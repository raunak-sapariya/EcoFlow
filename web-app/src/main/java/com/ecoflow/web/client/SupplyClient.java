package com.ecoflow.web.client;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

/** Calls supply-service (http://localhost:8083): zones, usage, distribution schedules. */
public class SupplyClient extends ApiClient {

	public record Zone(Long id, String name, String city, Integer population, Double dailyDemandMl, Long sourceId) {
	}

	public record Usage(Long id, String zoneName, LocalDate usageMonth, String category, Double volumeMl) {
	}

	public record Schedule(Long id, String zoneName, LocalDate supplyDate, LocalTime startTime, LocalTime endTime,
			Double allocatedMl, Double suppliedMl, String status) {
	}

	public record Plan(String sourceName, String availability, Double storagePercent, Double dailyDemandMl,
			Integer allocationPercent, Double suggestedMl, String rule) {
	}

	public record DailySupply(LocalDate date, Double plannedMl, Double suppliedMl) {
	}

	public record Summary(Integer zoneCount, Long population, Double dailyDemandMl, Double allocatedTodayMl,
			Long cyclesToday, LocalDate usageMonth, Map<String, Double> usageByCategory,
			List<DailySupply> lastSevenDays) {
	}

	public record ZoneForm(String name, String city, Integer population, Long sourceId, Double dailyDemandMl) {
	}

	public record UsageForm(Long zoneId, YearMonth usageMonth, String category, Double volumeMl) {
	}

	public record ScheduleForm(Long zoneId, LocalDate supplyDate, LocalTime startTime, LocalTime endTime,
			Double allocatedMl) {
	}

	private record UsagePayload(Long zoneId, LocalDate usageMonth, String category, Double volumeMl) {
	}

	public SupplyClient(RestClient rest) {
		super("supply-service", rest);
	}

	public List<Zone> zones() {
		return execute(() -> rest.get().uri("/api/zones").retrieve().body(new ParameterizedTypeReference<List<Zone>>() {
		}));
	}

	public Map<Long, String> zoneNames() {
		Map<Long, String> names = new HashMap<>();
		for (Zone zone : zones()) {
			names.put(zone.id(), zone.name());
		}
		return names;
	}

	public Zone zone(Long id) {
		return execute(() -> rest.get().uri("/api/zones/{id}", id).retrieve().body(Zone.class));
	}

	public void createZone(ZoneForm form) {
		executeVoid(() -> rest.post().uri("/api/zones").body(form).retrieve().toBodilessEntity());
	}

	public Plan plan(Long zoneId) {
		return execute(() -> rest.get().uri("/api/zones/{id}/plan", zoneId).retrieve().body(Plan.class));
	}

	public List<Usage> usage() {
		return execute(() -> rest.get().uri("/api/usage").retrieve().body(new ParameterizedTypeReference<List<Usage>>() {}));
	}

	public List<Usage> usageOfZone(Long zoneId) {
		return execute(() -> rest.get().uri("/api/usage?zoneId={id}", zoneId).retrieve().body(new ParameterizedTypeReference<List<Usage>>() {}));
	}

	public void addUsage(UsageForm form) {
		LocalDate month = form.usageMonth() == null ? null : form.usageMonth().atDay(1);
		executeVoid(() -> rest.post().uri("/api/usage").body(new UsagePayload(form.zoneId(), month, form.category(), form.volumeMl())).retrieve().toBodilessEntity());
	}

	public List<Schedule> schedules() {
		return execute(() -> rest.get().uri("/api/schedules").retrieve().body(new ParameterizedTypeReference<List<Schedule>>() {}));
	}

	public List<Schedule> schedulesOfZone(Long zoneId) {
		return execute(() -> rest.get().uri("/api/schedules?zoneId={id}", zoneId).retrieve().body(new ParameterizedTypeReference<List<Schedule>>() {}));
	}

	public void createSchedule(ScheduleForm form) {
		executeVoid(() -> rest.post().uri("/api/schedules").body(form).retrieve().toBodilessEntity());
	}

	public Summary summary() {
		return execute(() -> rest.get().uri("/api/summary").retrieve().body(Summary.class));
	}

}
