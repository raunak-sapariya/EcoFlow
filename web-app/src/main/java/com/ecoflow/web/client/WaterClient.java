package com.ecoflow.web.client;

import java.time.LocalDate;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

/**
 * Calls water-service (http://localhost:8082): sources, level readings, quality
 */
public class WaterClient extends ApiClient {

	public record Source(Long id, String name, String type, String district, Double capacityMl, String managedBy,
			LocalDate lastReadingDate, Double storageMl, Double storagePercent, String availability, Double inflowMld,
			Double outflowMld, Double daysOfSupply, String lastQualityResult) {
	}

	public record Reading(Long id, LocalDate readingDate, Double waterLevelM, Double storageMl, Double inflowMld,
			Double outflowMld, Double rainfallMm) {
	}

	public record QualityTest(Long id, String sourceName, LocalDate testedOn, Double ph, Double dissolvedOxygen,
			Double bod, Double turbidity, Double tds, Double totalColiform, String labName, String result,
			String remarks) {
	}

	public record Summary(Integer sourceCount, Double storagePercent, Long criticalCount, Long watchCount,
			Double compliancePercent, LocalDate latestReadingDate) {
	}

	public record SourceForm(String name, String type, String district, Double latitude, Double longitude,
			Double capacityMl, String managedBy) {
	}

	public record ReadingForm(LocalDate readingDate, Double waterLevelM, Double storageMl, Double inflowMld,
			Double outflowMld, Double rainfallMm) {
	}

	public record QualityForm(Long sourceId, LocalDate testedOn, Double ph, Double dissolvedOxygen, Double bod,
			Double turbidity, Double tds, Double totalColiform, String labName, String remarks) {
	}

	public WaterClient(RestClient rest) {
		super("water-service", rest);
	}

	public List<Source> sources() {
		return execute(() -> rest.get().uri("/api/sources").retrieve().body(new ParameterizedTypeReference<List<Source>>() {}));
	}

	public List<Source> criticalSources() {
		return execute(() -> rest.get().uri("/api/sources?availability=CRITICAL").retrieve().body(new ParameterizedTypeReference<List<Source>>() {}));
	}

	public Source source(Long id) {
		return execute(() -> rest.get().uri("/api/sources/{id}", id).retrieve().body(Source.class));
	}

	public void createSource(SourceForm form) {
		executeVoid(() -> rest.post().uri("/api/sources").body(form).retrieve().toBodilessEntity());
	}

	public List<Reading> readings(Long sourceId) {
		return execute(() -> rest.get().uri("/api/sources/{id}/readings", sourceId).retrieve().body(new ParameterizedTypeReference<List<Reading>>() {}));
	}

	public void addReading(Long sourceId, ReadingForm form) {
		executeVoid(() -> rest.post().uri("/api/sources/{id}/readings", sourceId).body(form).retrieve().toBodilessEntity());
	}

	public List<QualityTest> qualityTests() {
		return execute(() -> rest.get().uri("/api/quality-tests").retrieve().body(new ParameterizedTypeReference<List<QualityTest>>() {}));
	}

	public QualityTest recordTest(QualityForm form) {
		return execute(() -> rest.post().uri("/api/quality-tests").body(form).retrieve().body(QualityTest.class));
	}

	public Summary summary() {
		return execute(() -> rest.get().uri("/api/summary").retrieve().body(Summary.class));
	}

}
