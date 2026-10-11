package com.ecoflow.operations.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.ecoflow.operations.dto.MaintenanceRequest;
import com.ecoflow.operations.dto.MaintenanceUpdate;
import com.ecoflow.operations.model.Asset;
import com.ecoflow.operations.model.MaintenanceTask;
import com.ecoflow.operations.repository.MaintenanceTaskRepository;

//maintenance schedules, faults and repairs.

@Service
@Transactional
public class MaintenanceService {

	public static final List<String> OPEN_STATUSES = List.of("SCHEDULED", "IN_PROGRESS");

	private final MaintenanceTaskRepository tasks;
	private final AssetService assetService;

	public MaintenanceService(MaintenanceTaskRepository tasks, AssetService assetService) {
		this.tasks = tasks;
		this.assetService = assetService;
	}

	@Transactional(readOnly = true)
	public List<MaintenanceTask> list(Long zoneId, boolean openOnly) {
		List<MaintenanceTask> all = zoneId == null ? tasks.findAllByOrderByIdDesc()
				: tasks.findByAsset_ZoneIdOrderByIdDesc(zoneId);
		if (!openOnly) {
			return all;
		}
		return all.stream().filter(t -> OPEN_STATUSES.contains(t.getStatus())).toList();
	}

	@Transactional(readOnly = true)
	public long countOverdue() {
		return tasks.countByStatusInAndScheduledDateBefore(OPEN_STATUSES, LocalDate.now());
	}

	public MaintenanceTask create(MaintenanceRequest r) {
		Asset asset = assetService.get(r.assetId());
		if ("REPAIR".equals(r.type()) && "WORKING".equals(asset.getStatus())) {
			asset.setStatus("FAULTY");
		}
		return tasks.save(new MaintenanceTask(asset, r.type(), r.description(), r.scheduledDate(), r.technician(), r.cost()));
	}

	public MaintenanceTask update(Long id, MaintenanceUpdate r) {
		MaintenanceTask task = tasks.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Maintenance task " + id + " not found"));
		if ("DONE".equals(task.getStatus())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Task " + id + " is already done");
		}
		task.setStatus(r.status());
		Asset asset = task.getAsset();
		if ("IN_PROGRESS".equals(r.status())) {
			asset.setStatus("UNDER_REPAIR");
		}
		if ("DONE".equals(r.status())) {
			task.setCompletedDate(LocalDate.now());
			asset.setStatus("WORKING");
		}
		return task;
	}

}
