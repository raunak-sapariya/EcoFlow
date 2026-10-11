package com.ecoflow.supply.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ecoflow.supply.dto.SupplyPlan;
import com.ecoflow.supply.dto.ZoneRequest;
import com.ecoflow.supply.model.SupplyZone;
import com.ecoflow.supply.service.ZoneService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/zones")
public class ZoneController {

	private final ZoneService zoneService;

	public ZoneController(ZoneService zoneService) {
		this.zoneService = zoneService;
	}

	@GetMapping
	public List<SupplyZone> list() {
		return zoneService.list();
	}

	@GetMapping("/{id}")
	public SupplyZone get(@PathVariable Long id) {
		return zoneService.get(id);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public SupplyZone create(@Valid @RequestBody ZoneRequest request) {
		return zoneService.create(request);
	}

	@GetMapping("/{id}/plan")
	public SupplyPlan plan(@PathVariable Long id) {
		return zoneService.plan(id);
	}

}
