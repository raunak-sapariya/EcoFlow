package com.ecoflow.operations.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ecoflow.operations.dto.MaintenanceRequest;
import com.ecoflow.operations.dto.MaintenanceUpdate;
import com.ecoflow.operations.model.MaintenanceTask;
import com.ecoflow.operations.service.MaintenanceService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/maintenance")
public class MaintenanceController {

	private final MaintenanceService maintenanceService;

	public MaintenanceController(MaintenanceService maintenanceService) {
		this.maintenanceService = maintenanceService;
	}

	@GetMapping
	public List<MaintenanceTask> list(@RequestParam(required = false) Long zoneId,
			@RequestParam(defaultValue = "false") boolean openOnly) {
		return maintenanceService.list(zoneId, openOnly);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public MaintenanceTask create(@Valid @RequestBody MaintenanceRequest request) {
		return maintenanceService.create(request);
	}

	/** Change the status, e.g. {"status":"DONE"} */
	@PutMapping("/{id}")
	public MaintenanceTask update(@PathVariable Long id, @Valid @RequestBody MaintenanceUpdate request) {
		return maintenanceService.update(id, request);
	}

}
