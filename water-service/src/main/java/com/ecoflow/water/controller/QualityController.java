package com.ecoflow.water.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ecoflow.water.dto.QualityTestRequest;
import com.ecoflow.water.model.QualityTest;
import com.ecoflow.water.service.QualityService;

import jakarta.validation.Valid;

// quality monitoring
@RestController
@RequestMapping("/api/quality-tests")
public class QualityController {

	private final QualityService qualityService;

	public QualityController(QualityService qualityService) {
		this.qualityService = qualityService;
	}

	
	@GetMapping
	public List<QualityTest> list() {
		return qualityService.list();
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public QualityTest record(@Valid @RequestBody QualityTestRequest request) {
		return qualityService.record(request);
	}

}
