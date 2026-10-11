package com.ecoflow.supply.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ScheduleRequest(
		@NotNull Long zoneId,
		@NotNull LocalDate supplyDate,
		@NotNull LocalTime startTime,
		@NotNull LocalTime endTime,
		@NotNull @Positive Double allocatedMl) {
}
