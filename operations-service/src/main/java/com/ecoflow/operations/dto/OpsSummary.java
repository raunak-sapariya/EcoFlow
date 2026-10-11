package com.ecoflow.operations.dto;

public record OpsSummary(
		long assetCount,
		long assetsNotWorking,
		long maintenanceOpen,
		long maintenanceOverdue,
		long complaintsOpen,
		long supplyRequestsOpen,
		long alertCount) {
}
