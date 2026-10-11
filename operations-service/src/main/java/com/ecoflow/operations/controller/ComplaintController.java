package com.ecoflow.operations.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ecoflow.operations.dto.ComplaintRequest;
import com.ecoflow.operations.dto.ComplaintUpdate;
import com.ecoflow.operations.model.Complaint;
import com.ecoflow.operations.service.ComplaintService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/complaints")
public class ComplaintController {

	private final ComplaintService complaintService;

	public ComplaintController(ComplaintService complaintService) {
		this.complaintService = complaintService;
	}

	@GetMapping
	public List<Complaint> list(@RequestParam(required = false) Long zoneId) {
		return complaintService.list(zoneId);
	}

	@GetMapping("/{id}")
	public Complaint get(@PathVariable Long id) {
		return complaintService.get(id);
	}

	@GetMapping("/track")
	public Complaint track(@RequestParam Long id, @RequestParam String phone) {
		return complaintService.track(id, phone);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Complaint create(@Valid @RequestBody ComplaintRequest request) {
		return complaintService.create(request);
	}

	@PutMapping("/{id}")
	public Complaint update(@PathVariable Long id, @Valid @RequestBody ComplaintUpdate request) {
		return complaintService.update(id, request);
	}

}
