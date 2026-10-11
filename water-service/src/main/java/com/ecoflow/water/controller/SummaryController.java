package com.ecoflow.water.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecoflow.water.dto.WaterSummary;
import com.ecoflow.water.service.SourceService;

// dashboard numbers
@RestController
public class SummaryController {

	private final SourceService sourceService;

	public SummaryController(SourceService sourceService) {
		this.sourceService = sourceService;
	}

	@GetMapping("/api/summary")
	public WaterSummary summary() {
		return sourceService.summary();
	}

}
