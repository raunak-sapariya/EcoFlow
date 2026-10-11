package com.ecoflow.water.config;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ecoflow.water.model.QualityTest;
import com.ecoflow.water.model.WaterReading;
import com.ecoflow.water.model.WaterSource;
import com.ecoflow.water.repository.QualityTestRepository;
import com.ecoflow.water.repository.WaterReadingRepository;
import com.ecoflow.water.repository.WaterSourceRepository;

@Component
public class CsvDataLoader implements ApplicationRunner {

	private static final Logger log = LoggerFactory.getLogger(CsvDataLoader.class);

	
	private static final CSVFormat CSV = CSVFormat.DEFAULT.builder()
			.setHeader().setSkipHeaderRecord(true).setCommentMarker('#').get();

	private final WaterSourceRepository sources;
	private final WaterReadingRepository readings;
	private final QualityTestRepository tests;

	public CsvDataLoader(WaterSourceRepository sources, WaterReadingRepository readings, QualityTestRepository tests) {
		this.sources = sources;
		this.readings = readings;
		this.tests = tests;
	}

	@Override
	@Transactional
	public void run(ApplicationArguments args) throws IOException {
		if (sources.count() > 0) {
			return; 
		}

	
		Map<String, WaterSource> sourceById = new HashMap<>();
		for (CSVRecord row : read("data/water_sources.csv")) {
			WaterSource source = new WaterSource(row.get("name"), row.get("type"), row.get("district"),
					number(row, "latitude"), number(row, "longitude"), number(row, "capacity_ml"), text(row, "managed_by"));
			sourceById.put(row.get("id"), sources.save(source));
		}

		for (CSVRecord row : read("data/water_readings.csv")) {
			readings.save(new WaterReading(sourceById.get(row.get("source_id")), LocalDate.parse(row.get("reading_date")),
					number(row, "water_level_m"), number(row, "storage_ml"), number(row, "inflow_mld"),
					number(row, "outflow_mld"), number(row, "rainfall_mm")));
		}

		for (CSVRecord row : read("data/quality_tests.csv")) {
			tests.save(new QualityTest(sourceById.get(row.get("source_id")), LocalDate.parse(row.get("tested_on")),
					number(row, "ph"), number(row, "dissolved_oxygen"), number(row, "bod"), number(row, "turbidity"),
					number(row, "tds"), number(row, "total_coliform"), text(row, "lab_name"), row.get("result"),
					text(row, "remarks")));
		}

		log.info("Loaded {} water sources, {} readings and {} quality tests from CSV", sources.count(), readings.count(),
				tests.count());
	}


	private static List<CSVRecord> read(String path) throws IOException {
		try (Reader reader = new InputStreamReader(new ClassPathResource(path).getInputStream(), StandardCharsets.UTF_8)) {
			return CSV.parse(reader).getRecords();
		}
	}

	private static Double number(CSVRecord row, String column) {
		String value = row.get(column);
		return value.isBlank() ? null : Double.valueOf(value);
	}

	private static String text(CSVRecord row, String column) {
		String value = row.get(column);
		return value.isBlank() ? null : value;
	}

}
