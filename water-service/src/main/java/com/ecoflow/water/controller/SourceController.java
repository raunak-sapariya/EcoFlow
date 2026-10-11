package com.ecoflow.water.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ecoflow.water.dto.ReadingRequest;
import com.ecoflow.water.dto.SourceRequest;
import com.ecoflow.water.dto.SourceStatus;
import com.ecoflow.water.model.WaterReading;
import com.ecoflow.water.model.WaterSource;
import com.ecoflow.water.service.SourceService;

import jakarta.validation.Valid;


// resource and availabality management
@RestController
@RequestMapping("/api/sources")
public class SourceController {

	private final SourceService sourceService;

	public SourceController(SourceService sourceService) {
		this.sourceService = sourceService;
	}


	@GetMapping
	public List<SourceStatus> list(@RequestParam(required = false) String availability) {
		return sourceService.listSources(availability);
	}

	
	@GetMapping("/{id}")
	public SourceStatus get(@PathVariable Long id) {
		return sourceService.getSource(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public WaterSource create(@Valid @RequestBody SourceRequest request) {
		return sourceService.create(request);
	}

	
	@GetMapping("/{id}/readings")
	public List<WaterReading> readings(@PathVariable Long id) {
		return sourceService.readings(id);
	}

	@PostMapping("/{id}/readings")
	@ResponseStatus(HttpStatus.CREATED)
	public WaterReading addReading(@PathVariable Long id, @Valid @RequestBody ReadingRequest request) {
		return sourceService.addReading(id, request);
	}

}
