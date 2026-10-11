package com.ecoflow.operations.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ecoflow.operations.dto.AlertRequest;
import com.ecoflow.operations.model.Alert;
import com.ecoflow.operations.service.AlertService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {

	private final AlertService alertService;

	public AlertController(AlertService alertService) {
		this.alertService = alertService;
	}

	@GetMapping
	public List<Alert> list() {
		return alertService.list();
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Alert create(@Valid @RequestBody AlertRequest request) {
		return alertService.create(request);
	}

}
