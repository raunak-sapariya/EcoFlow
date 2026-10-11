package com.ecoflow.water.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

// /** JSON body of POST /api/sources. 
public record SourceRequest(
		@NotBlank String name,
		@NotBlank @Pattern(regexp = "RESERVOIR|LAKE|WELL|TANK") String type,
		@NotBlank String district,
		Double latitude,
		Double longitude,
		@NotNull @Positive Double capacityMl,
		String managedBy) {
}
