package com.ecoflow.operations.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecoflow.operations.dto.AlertRequest;
import com.ecoflow.operations.model.Alert;
import com.ecoflow.operations.repository.AlertRepository;

//alerts are written by staff through a form and simply saved.
@Service
@Transactional
public class AlertService {

	private final AlertRepository alerts;

	public AlertService(AlertRepository alerts) {
		this.alerts = alerts;
	}

	@Transactional(readOnly = true)
	public List<Alert> list() {
		return alerts.findAllByOrderByIdDesc();
	}

	public Alert create(AlertRequest request) {
		return alerts.save(new Alert(request.title(), request.message(), request.severity(), request.createdBy()));
	}

	@Transactional(readOnly = true)
	public long count() {
		return alerts.count();
	}

}
