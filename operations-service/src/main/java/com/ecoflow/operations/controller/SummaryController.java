package com.ecoflow.operations.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecoflow.operations.dto.OpsSummary;
import com.ecoflow.operations.service.SummaryService;

@RestController
public class SummaryController {

	private final SummaryService summaryService;

	public SummaryController(SummaryService summaryService) {
		this.summaryService = summaryService;
	}

	@GetMapping("/api/summary")
	public OpsSummary summary() {
		return summaryService.summary();
	}

}
