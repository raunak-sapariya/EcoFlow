package com.ecoflow.operations.config;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

import com.ecoflow.operations.model.Alert;
import com.ecoflow.operations.model.Asset;
import com.ecoflow.operations.model.Complaint;
import com.ecoflow.operations.model.MaintenanceTask;
import com.ecoflow.operations.repository.AlertRepository;
import com.ecoflow.operations.repository.AssetRepository;
import com.ecoflow.operations.repository.ComplaintRepository;
import com.ecoflow.operations.repository.MaintenanceTaskRepository;

// first -copies the CSV files into the empty operations_db tables.
// dates in the csv files are relative ("days_from_today", "created_hours_ago")
@Component
public class CsvDataLoader implements ApplicationRunner {

	private static final Logger log = LoggerFactory.getLogger(CsvDataLoader.class);

	private static final CSVFormat CSV = CSVFormat.DEFAULT.builder()
			.setHeader().setSkipHeaderRecord(true).setCommentMarker('#').get();

	private final AssetRepository assets;
	private final MaintenanceTaskRepository tasks;
	private final ComplaintRepository complaints;
	private final AlertRepository alerts;

	public CsvDataLoader(AssetRepository assets, MaintenanceTaskRepository tasks, ComplaintRepository complaints,
			AlertRepository alerts) {
		this.assets = assets;
		this.tasks = tasks;
		this.complaints = complaints;
		this.alerts = alerts;
	}

	@Override
	@Transactional
	public void run(ApplicationArguments args) throws IOException {
		LocalDate today = LocalDate.now();
		LocalDateTime now = LocalDateTime.now();

		if (assets.count() == 0) {
			Map<String, Asset> assetById = new HashMap<>();
			for (CSVRecord row : read("data/assets.csv")) {
				Asset asset = new Asset(row.get("name"), row.get("type"), Long.valueOf(row.get("zone_id")),
						longOrNull(row, "source_id"), LocalDate.parse(row.get("install_date")), row.get("condition"),
						row.get("status"));
				assetById.put(row.get("id"), assets.save(asset));
			}

			for (CSVRecord row : read("data/maintenance_tasks.csv")) {
				MaintenanceTask task = new MaintenanceTask(assetById.get(row.get("asset_id")), row.get("type"),
						row.get("description"), today.plusDays(Long.parseLong(row.get("days_from_today"))),
						text(row, "technician"), row.get("cost").isBlank() ? null : Double.valueOf(row.get("cost")));
				task.setStatus(row.get("status"));
				if (!row.get("completed_days_from_today").isBlank()) {
					task.setCompletedDate(today.plusDays(Long.parseLong(row.get("completed_days_from_today"))));
				}
				tasks.save(task);
			}

			for (CSVRecord row : read("data/complaints.csv")) {
				Asset asset = row.get("asset_id").isBlank() ? null : assetById.get(row.get("asset_id"));
				Complaint complaint = new Complaint(row.get("category"), row.get("description"),
						Long.valueOf(row.get("zone_id")), asset, row.get("citizen_name"), row.get("phone"),
						text(row, "address"), row.get("priority"));
				complaint.setStatus(row.get("status"));
				complaint.setResolutionNote(text(row, "resolution_note"));
				complaint.setCreatedAt(now.minusHours(Long.parseLong(row.get("created_hours_ago"))));
				if (!row.get("resolved_hours_ago").isBlank()) {
					complaint.setResolvedAt(now.minusHours(Long.parseLong(row.get("resolved_hours_ago"))));
				}
				complaints.save(complaint);
			}
			log.info("Loaded {} assets, {} maintenance tasks and {} complaints from CSV", assets.count(), tasks.count(),
					complaints.count());
		}

		if (alerts.count() == 0) {
			for (CSVRecord row : read("data/alerts.csv")) {
				Alert alert = new Alert(row.get("title"), row.get("message"), row.get("severity"), text(row, "created_by"));
				alert.setCreatedAt(now.minusDays(Long.parseLong(row.get("days_ago"))));
				alerts.save(alert);
			}
			log.info("Loaded {} alerts from CSV", alerts.count());
		}
	}

	private static List<CSVRecord> read(String path) throws IOException {
		try (Reader reader = new InputStreamReader(new ClassPathResource(path).getInputStream(), StandardCharsets.UTF_8)) {
			return CSV.parse(reader).getRecords();
		}
	}

	private static String text(CSVRecord row, String column) {
		String value = row.get(column);
		return value.isBlank() ? null : value;
	}

	private static Long longOrNull(CSVRecord row, String column) {
		String value = row.get(column);
		return value.isBlank() ? null : Long.valueOf(value);
	}

}
