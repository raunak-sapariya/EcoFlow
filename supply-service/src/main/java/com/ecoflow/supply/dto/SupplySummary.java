package com.ecoflow.supply.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

// Represents the overall water-supply statistics

public record SupplySummary(
		int zoneCount,
		long population,
		double dailyDemandMl,
		double allocatedTodayMl,
		long cyclesToday,
		LocalDate usageMonth,                 
		Map<String, Double> usageByCategory,   
		List<DailySupply> lastSevenDays) {      
}
