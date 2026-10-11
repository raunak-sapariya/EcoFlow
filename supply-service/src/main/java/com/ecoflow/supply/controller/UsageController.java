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

import com.ecoflow.supply.dto.UsageRequest;
import com.ecoflow.supply.model.UsageRecord;
import com.ecoflow.supply.service.UsageService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/usage")
public class UsageController {

	private final UsageService usageService;

	public UsageController(UsageService usageService) {
		this.usageService = usageService;
	}

	@GetMapping
	public List<UsageRecord> list(@RequestParam(required = false) Long zoneId) {
		return usageService.list(zoneId);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UsageRecord add(@Valid @RequestBody UsageRequest request) {
		return usageService.add(request);
	}

}
