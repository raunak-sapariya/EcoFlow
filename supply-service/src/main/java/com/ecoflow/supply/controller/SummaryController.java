package com.ecoflow.supply.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecoflow.supply.dto.SupplySummary;
import com.ecoflow.supply.service.SummaryService;

@RestController
public class SummaryController {

	private final SummaryService summaryService;

	public SummaryController(SummaryService summaryService) {
		this.summaryService = summaryService;
	}

	@GetMapping("/api/summary")
	public SupplySummary summary() {
		return summaryService.summary();
	}

}
