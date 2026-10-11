package com.ecoflow.supply.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ZoneRequest(
		@NotBlank String name,
		@NotBlank String city,
		@NotNull @Positive Integer population,
		@NotNull Long sourceId,
		@Positive Double dailyDemandMl) {
}
