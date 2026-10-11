package com.ecoflow.operations.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record AssetRequest(
		@NotBlank String name,
		@NotBlank @Pattern(regexp = "PUMP|PIPELINE|TREATMENT_PLANT|TANK|VALVE") String type,
		@NotNull Long zoneId,
		Long sourceId,
		LocalDate installDate,
		@NotBlank @Pattern(regexp = "GOOD|FAIR|POOR") String condition,
		@NotBlank @Pattern(regexp = "WORKING|FAULTY|UNDER_REPAIR") String status) {
}
