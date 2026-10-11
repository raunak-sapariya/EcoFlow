package com.ecoflow.water.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.ecoflow.water.dto.ReadingRequest;
import com.ecoflow.water.dto.SourceRequest;
import com.ecoflow.water.dto.SourceStatus;
import com.ecoflow.water.dto.WaterSummary;
import com.ecoflow.water.model.QualityTest;
import com.ecoflow.water.model.WaterReading;
import com.ecoflow.water.model.WaterSource;
import com.ecoflow.water.repository.QualityTestRepository;
import com.ecoflow.water.repository.WaterReadingRepository;
import com.ecoflow.water.repository.WaterSourceRepository;

// Water sources, level readings and availability no.
@Service
@Transactional
public class SourceService {

	
	public static final double WATCH_BELOW_PERCENT = 50;
	public static final double CRITICAL_BELOW_PERCENT = 30;

	private final WaterSourceRepository sources;
	private final WaterReadingRepository readings;
	private final QualityTestRepository tests;

	public SourceService(WaterSourceRepository sources, WaterReadingRepository readings, QualityTestRepository tests) {
		this.sources = sources;
		this.readings = readings;
		this.tests = tests;
	}

	public static String availability(Double storagePercent) {
		if (storagePercent == null) {
			return "NO_DATA";
		}
		if (storagePercent < CRITICAL_BELOW_PERCENT) {
			return "CRITICAL";
		}
		if (storagePercent < WATCH_BELOW_PERCENT) {
			return "WATCH";
		}
		return "NORMAL";
	}

	
	@Transactional(readOnly = true)
	public List<SourceStatus> listSources(String availability) {
		List<SourceStatus> result = new ArrayList<>();
		for (WaterSource source : sources.findAllByOrderByIdDesc()) {
			SourceStatus status = toStatus(source);
			if (availability == null || availability.equals(status.availability())) {
				result.add(status);
			}
		}
		return result;
	}

	@Transactional(readOnly = true)
	public SourceStatus getSource(Long id) {
		return toStatus(findSource(id));
	}

	public WaterSource create(SourceRequest request) {
		WaterSource source = new WaterSource(request.name(), request.type(), request.district(), request.latitude(),
				request.longitude(), request.capacityMl(), request.managedBy());
		return sources.save(source);
	}

	

	@Transactional(readOnly = true)
	public List<WaterReading> readings(Long sourceId) {
		findSource(sourceId); 
		return readings.findTop30BySource_IdOrderByIdDesc(sourceId);
	}

	public WaterReading addReading(Long sourceId, ReadingRequest request) {
		WaterSource source = findSource(sourceId);
		if (request.storageMl() > source.getCapacityMl()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Storage " + request.storageMl()
					+ " ML is more than the capacity of " + source.getName() + " (" + source.getCapacityMl() + " ML)");
		}
		WaterReading reading = new WaterReading(source, request.readingDate(), request.waterLevelM(),
				request.storageMl(), request.inflowMld(), request.outflowMld(), request.rainfallMm());
		return readings.save(reading);
	}


	@Transactional(readOnly = true)
	public WaterSummary summary() {
		List<SourceStatus> all = listSources(null);
		double capacity = 0;
		double storage = 0;
		long normal = 0, watch = 0, critical = 0, tested = 0, passing = 0;
		LocalDate latest = null;
		for (SourceStatus s : all) {
			if (s.storageMl() != null) { // only sources that report readings count towards the total
				capacity += s.capacityMl();
				storage += s.storageMl();
			}
			if ("NORMAL".equals(s.availability())) {
				normal++;
			}
			if ("WATCH".equals(s.availability())) {
				watch++;
			}
			if ("CRITICAL".equals(s.availability())) {
				critical++;
			}
			if (s.lastQualityResult() != null) {
				tested++;
				if ("PASS".equals(s.lastQualityResult())) {
					passing++;
				}
			}
			if (s.lastReadingDate() != null && (latest == null || s.lastReadingDate().isAfter(latest))) {
				latest = s.lastReadingDate();
			}
		}
		double percent = capacity == 0 ? 0 : storage * 100 / capacity;
		double compliance = tested == 0 ? 0 : passing * 100.0 / tested;
		return new WaterSummary(all.size(), round1(capacity), round1(storage), round1(percent), normal, watch, critical,
				tested, passing, round1(compliance), latest);
	}


	private WaterSource findSource(Long id) {
		return sources.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Source " + id + " not found"));
	}

	private SourceStatus toStatus(WaterSource s) {
		Optional<WaterReading> latest = readings.findFirstBySource_IdOrderByReadingDateDescIdDesc(s.getId());
		Optional<QualityTest> lastTest = tests.findFirstBySource_IdOrderByTestedOnDescIdDesc(s.getId());

		Double storage = latest.map(WaterReading::getStorageMl).orElse(null);
		Double percent = storage == null ? null : round1(storage * 100 / s.getCapacityMl());
		Double outflow = latest.map(WaterReading::getOutflowMld).orElse(null);
		
		Double daysOfSupply = (storage != null && outflow != null && outflow > 0) ? round1(storage / outflow) : null;

		return new SourceStatus(s.getId(), s.getName(), s.getType(), s.getDistrict(), s.getLatitude(),
				s.getLongitude(), s.getCapacityMl(), s.getManagedBy(), s.getStatus(),
				latest.map(WaterReading::getReadingDate).orElse(null),
				latest.map(WaterReading::getWaterLevelM).orElse(null),
				storage, percent, availability(percent),
				latest.map(WaterReading::getInflowMld).orElse(null), outflow, daysOfSupply,
				lastTest.map(QualityTest::getTestedOn).orElse(null),
				lastTest.map(QualityTest::getResult).orElse(null));
	}

	private static double round1(double value) {
		return Math.round(value * 10) / 10.0;
	}

}
