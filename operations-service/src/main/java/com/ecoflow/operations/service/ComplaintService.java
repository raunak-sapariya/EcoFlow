package com.ecoflow.operations.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.ecoflow.operations.dto.ComplaintRequest;
import com.ecoflow.operations.dto.ComplaintUpdate;
import com.ecoflow.operations.model.Complaint;
import com.ecoflow.operations.repository.ComplaintRepository;

//complaint & request management
@Service
@Transactional
public class ComplaintService {

	public static final List<String> OPEN_STATUSES = List.of("OPEN", "IN_PROGRESS");

	private final ComplaintRepository complaints;

	public ComplaintService(ComplaintRepository complaints) {
		this.complaints = complaints;
	}

//business rule
	public static String priorityFor(String category) {
		return switch (category) {
			case "LEAKAGE", "NO_SUPPLY", "QUALITY" -> "HIGH";
			case "LOW_PRESSURE", "SUPPLY_REQUEST" -> "MEDIUM";
			default -> "LOW";
		};
	}

	@Transactional(readOnly = true)
	public List<Complaint> list(Long zoneId) {
		if (zoneId == null) {
			return complaints.findAllByOrderByCreatedAtDesc();
		}
		return complaints.findByZoneIdAndStatusInOrderByCreatedAtDesc(zoneId, OPEN_STATUSES);
	}

	@Transactional(readOnly = true)
	public Complaint get(Long id) {
		return complaints.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Complaint " + id + " not found"));
	}

	public Complaint create(ComplaintRequest r) {
		Complaint complaint = new Complaint(r.category(), r.description(), r.zoneId(), null, r.citizenName(), r.phone(),
				r.address(), priorityFor(r.category()));
		return complaints.save(complaint);
	}

	@Transactional(readOnly = true)
	public Complaint track(Long id, String phone) {
		Complaint complaint = complaints.findById(id).orElse(null);
		if (complaint == null || !complaint.getPhone().equals(phone)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No ticket #" + id + " for this phone number");
		}
		return complaint;
	}

	public Complaint update(Long id, ComplaintUpdate r) {
		Complaint complaint = get(id);
		if ("RESOLVED".equals(r.status()) && (r.resolutionNote() == null || r.resolutionNote().isBlank())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Write a resolution note before resolving");
		}
		complaint.setStatus(r.status());
		complaint.setPriority(r.priority());
		complaint.setResolutionNote(r.resolutionNote());
		complaint.setResolvedAt("RESOLVED".equals(r.status()) ? LocalDateTime.now() : null);
		return complaint;
	}

}
