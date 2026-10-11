package com.ecoflow.supply.config;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
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

import com.ecoflow.supply.model.DistributionSchedule;
import com.ecoflow.supply.model.SupplyZone;
import com.ecoflow.supply.model.UsageRecord;
import com.ecoflow.supply.repository.DistributionScheduleRepository;
import com.ecoflow.supply.repository.SupplyZoneRepository;
import com.ecoflow.supply.repository.UsageRecordRepository;

@Component
public class CsvDataLoader implements ApplicationRunner {

	private static final Logger log = LoggerFactory.getLogger(CsvDataLoader.class);

	/** Comma separated, first line = column names, lines starting with # are comments. */
	private static final CSVFormat CSV = CSVFormat.DEFAULT.builder()
			.setHeader().setSkipHeaderRecord(true).setCommentMarker('#').get();

	private final SupplyZoneRepository zones;
	private final UsageRecordRepository usage;
	private final DistributionScheduleRepository schedules;

	public CsvDataLoader(SupplyZoneRepository zones, UsageRecordRepository usage, DistributionScheduleRepository schedules) {
		this.zones = zones;
		this.usage = usage;
		this.schedules = schedules;
	}

	@Override
	@Transactional
	public void run(ApplicationArguments args) throws IOException {
		if (zones.count() > 0) {
			return; // only ever fill an empty database
		}
		LocalDate today = LocalDate.now();

		Map<String, SupplyZone> zoneById = new HashMap<>();
		for (CSVRecord row : read("data/supply_zones.csv")) {
			SupplyZone zone = new SupplyZone(row.get("name"), row.get("city"), Integer.parseInt(row.get("population")),
					Double.parseDouble(row.get("daily_demand_ml")), Long.valueOf(row.get("source_id")));
			zoneById.put(row.get("id"), zones.save(zone));
		}

		LocalDate thisMonth = today.withDayOfMonth(1);
		for (CSVRecord row : read("data/usage_records.csv")) {
			LocalDate month = thisMonth.minusMonths(Long.parseLong(row.get("months_ago")));
			usage.save(new UsageRecord(zoneById.get(row.get("zone_id")), month, row.get("category"),
					Double.parseDouble(row.get("volume_ml"))));
		}

		for (CSVRecord row : read("data/distribution_schedules.csv")) {
			DistributionSchedule schedule = new DistributionSchedule(zoneById.get(row.get("zone_id")),
					today.plusDays(Long.parseLong(row.get("days_from_today"))), LocalTime.parse(row.get("start_time")),
					LocalTime.parse(row.get("end_time")), Double.parseDouble(row.get("allocated_ml")));
			if ("COMPLETED".equals(row.get("status"))) {
				schedule.complete(Double.parseDouble(row.get("supplied_ml")));
			}
			if ("CANCELLED".equals(row.get("status"))) {
				schedule.cancel();
			}
			schedules.save(schedule);
		}

		log.info("Loaded {} zones, {} usage records and {} supply cycles from CSV", zones.count(), usage.count(),
				schedules.count());
	}

	private static List<CSVRecord> read(String path) throws IOException {
		try (Reader reader = new InputStreamReader(new ClassPathResource(path).getInputStream(), StandardCharsets.UTF_8)) {
			return CSV.parse(reader).getRecords();
		}
	}

}
