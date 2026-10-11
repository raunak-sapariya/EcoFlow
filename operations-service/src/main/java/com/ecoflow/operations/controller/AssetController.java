package com.ecoflow.operations.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ecoflow.operations.dto.AssetRequest;
import com.ecoflow.operations.model.Asset;
import com.ecoflow.operations.service.AssetService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/assets")
public class AssetController {

	private final AssetService assetService;

	public AssetController(AssetService assetService) {
		this.assetService = assetService;
	}

	@GetMapping
	public List<Asset> list(@RequestParam(required = false) Long zoneId) {
		return assetService.list(zoneId);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Asset create(@Valid @RequestBody AssetRequest request) {
		return assetService.create(request);
	}

}
