package com.ecoflow.water.dto;

import java.time.LocalDate;

//  water numbers on the dashboard. 
public record WaterSummary(
		int sourceCount,
		double totalCapacityMl,
		double totalStorageMl,
		double storagePercent,
		long normalCount,
		long watchCount,
		long criticalCount,
		long testedSources,
		long passingSources,
		double compliancePercent,
		LocalDate latestReadingDate) {
}
