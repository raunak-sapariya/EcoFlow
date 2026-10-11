package com.ecoflow.water.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;

// test_quality json
public record QualityTestRequest(
		@NotNull Long sourceId,
		@NotNull @PastOrPresent LocalDate testedOn,
		@NotNull @PositiveOrZero Double ph,
		@NotNull @PositiveOrZero Double dissolvedOxygen,
		@NotNull @PositiveOrZero Double bod,
		@PositiveOrZero Double turbidity,
		@PositiveOrZero Double tds,
		@PositiveOrZero Double totalColiform,
		String labName,
		String remarks) {
}
