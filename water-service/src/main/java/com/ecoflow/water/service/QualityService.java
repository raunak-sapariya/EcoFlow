package com.ecoflow.water.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.ecoflow.water.dto.QualityTestRequest;
import com.ecoflow.water.model.QualityTest;
import com.ecoflow.water.model.WaterSource;
import com.ecoflow.water.repository.QualityTestRepository;
import com.ecoflow.water.repository.WaterSourceRepository;


@Service
@Transactional
public class QualityService {

	public static final double PH_MIN = 6.0;
	public static final double PH_MAX = 9.0;
	public static final double DO_MIN = 4.0;
	public static final double BOD_MAX = 3.0;
	public static final double COLIFORM_MAX = 5000;

	private final QualityTestRepository tests;
	private final WaterSourceRepository sources;

	public QualityService(QualityTestRepository tests, WaterSourceRepository sources) {
		this.tests = tests;
		this.sources = sources;
	}


	public static List<String> breaches(double ph, double dissolvedOxygen, double bod, Double totalColiform) {
		List<String> problems = new ArrayList<>();
		if (ph < PH_MIN || ph > PH_MAX) {
			problems.add("pH " + ph + " outside " + PH_MIN + "-" + PH_MAX);
		}
		if (dissolvedOxygen < DO_MIN) {
			problems.add("DO " + dissolvedOxygen + " mg/L < " + DO_MIN);
		}
		if (bod > BOD_MAX) {
			problems.add("BOD " + bod + " mg/L > " + BOD_MAX);
		}
		if (totalColiform != null && totalColiform > COLIFORM_MAX) {
			problems.add("Total coliform " + totalColiform + " MPN/100mL > " + COLIFORM_MAX);
		}
		return problems;
	}

	public QualityTest record(QualityTestRequest r) {
		WaterSource source = sources.findById(r.sourceId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Source " + r.sourceId() + " does not exist"));
		List<String> problems = breaches(r.ph(), r.dissolvedOxygen(), r.bod(), r.totalColiform());
		String result = problems.isEmpty() ? "PASS" : "FAIL";
		String remarks = joinRemarks(problems, r.remarks());
		QualityTest test = new QualityTest(source, r.testedOn(), r.ph(), r.dissolvedOxygen(), r.bod(), r.turbidity(),
				r.tds(), r.totalColiform(), r.labName(), result, remarks);
		return tests.save(test);
	}
// all tests
	@Transactional(readOnly = true)
	public List<QualityTest> list() {
		return tests.findAllByOrderByIdDesc();
	}

	static String joinRemarks(List<String> problems, String remarks) {
		String text = String.join("; ", problems);
		if (remarks != null && !remarks.isBlank()) {
			text = text.isEmpty() ? remarks : text + ". " + remarks;
		}
		return text.isEmpty() ? null : text;
	}

}
