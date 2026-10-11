package com.ecoflow.operations.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecoflow.operations.dto.OpsSummary;
import com.ecoflow.operations.model.Asset;
import com.ecoflow.operations.model.Complaint;

@Service
@Transactional(readOnly = true)
public class SummaryService {

	private final AssetService assetService;
	private final MaintenanceService maintenanceService;
	private final ComplaintService complaintService;
	private final AlertService alertService;

	public SummaryService(AssetService assetService, MaintenanceService maintenanceService,
			ComplaintService complaintService, AlertService alertService) {
		this.assetService = assetService;
		this.maintenanceService = maintenanceService;
		this.complaintService = complaintService;
		this.alertService = alertService;
	}

	public OpsSummary summary() {
		long assetCount = 0;
		long notWorking = 0;
		for (Asset a : assetService.list(null)) {
			assetCount++;
			if (!"WORKING".equals(a.getStatus())) {
				notWorking++;
			}
		}

		long openComplaints = 0;
		long openRequests = 0;
		for (Complaint c : complaintService.list(null)) {
			if (ComplaintService.OPEN_STATUSES.contains(c.getStatus())) {
				openComplaints++;
				if ("SUPPLY_REQUEST".equals(c.getCategory())) {
					openRequests++;
				}
			}
		}

		return new OpsSummary(assetCount, notWorking, maintenanceService.list(null, true).size(),
				maintenanceService.countOverdue(), openComplaints, openRequests, alertService.count());
	}

}
