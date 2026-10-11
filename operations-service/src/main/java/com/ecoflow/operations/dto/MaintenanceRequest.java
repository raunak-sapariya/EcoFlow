package com.ecoflow.operations.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

public record MaintenanceRequest(
		@NotNull Long assetId,
		@NotBlank @Pattern(regexp = "PREVENTIVE|REPAIR") String type,
		@NotBlank String description,
		@NotNull LocalDate scheduledDate,
		String technician,
		@PositiveOrZero Double cost) {
}
