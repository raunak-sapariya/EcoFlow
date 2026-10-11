package com.ecoflow.supply.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

public record UsageRequest(
		@NotNull Long zoneId,
		@NotNull LocalDate usageMonth,
		@NotNull @Pattern(regexp = "DOMESTIC|AGRICULTURAL|INDUSTRIAL|INSTITUTIONAL") String category,
		@NotNull @Positive Double volumeMl) {
}
