package com.ecoflow.water.dto;

import java.time.LocalDate;


public record SourceStatus(
		Long id,
		String name,
		String type,
		String district,
		Double latitude,
		Double longitude,
		double capacityMl,
		String managedBy,
		String status,
		LocalDate lastReadingDate,
		Double waterLevelM,
		Double storageMl,
		Double storagePercent,
		String availability,
		Double inflowMld,
		Double outflowMld,
		Double daysOfSupply,
		LocalDate lastTestedOn,
		String lastQualityResult) {
}
