package com.ecoflow.supply.dto;

// Represents the recommended water allocation plan for a supply zone, based on the current availability of its water source.

public record SupplyPlan(
		Long zoneId,
		String zoneName,
		Long sourceId,
		String sourceName,
		String availability,
		Double storagePercent,
		double dailyDemandMl,
		int allocationPercent,
		double suggestedMl,
		String rule) {
}
