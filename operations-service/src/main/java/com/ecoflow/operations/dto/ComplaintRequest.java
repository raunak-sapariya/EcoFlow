package com.ecoflow.operations.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ComplaintRequest(
		@NotBlank @Pattern(regexp = "LEAKAGE|NO_SUPPLY|LOW_PRESSURE|QUALITY|SUPPLY_REQUEST|OTHER") String category,
		@NotBlank @Size(max = 500) String description,
		@NotNull Long zoneId,
		@NotBlank @Size(max = 100) String citizenName,
		@NotBlank @Pattern(regexp = "[0-9]{10}", message = "must be a 10-digit mobile number") String phone,
		@Size(max = 200) String address) {
}
