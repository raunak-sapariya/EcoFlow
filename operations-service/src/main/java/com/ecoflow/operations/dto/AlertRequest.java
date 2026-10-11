package com.ecoflow.operations.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AlertRequest(
		@NotBlank @Size(max = 120) String title,
		@NotBlank @Size(max = 500) String message,
		@NotBlank @Pattern(regexp = "HIGH|MEDIUM|LOW") String severity,
		@Size(max = 100) String createdBy) {
}
