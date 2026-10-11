package com.ecoflow.water.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;

// json for readings
public record ReadingRequest(
		@NotNull @PastOrPresent LocalDate readingDate,
		Double waterLevelM,
		@NotNull @PositiveOrZero Double storageMl,
		@PositiveOrZero Double inflowMld,
		@PositiveOrZero Double outflowMld,
		@PositiveOrZero Double rainfallMm) {
}
