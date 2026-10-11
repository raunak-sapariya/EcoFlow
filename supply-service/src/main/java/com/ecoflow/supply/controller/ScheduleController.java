package com.ecoflow.supply.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ecoflow.supply.dto.ScheduleRequest;
import com.ecoflow.supply.model.DistributionSchedule;
import com.ecoflow.supply.service.ScheduleService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

	private final ScheduleService scheduleService;

	public ScheduleController(ScheduleService scheduleService) {
		this.scheduleService = scheduleService;
	}

	@GetMapping
	public List<DistributionSchedule> list(@RequestParam(required = false) Long zoneId) {
		return scheduleService.list(zoneId);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public DistributionSchedule create(@Valid @RequestBody ScheduleRequest request) {
		return scheduleService.create(request);
	}

}
