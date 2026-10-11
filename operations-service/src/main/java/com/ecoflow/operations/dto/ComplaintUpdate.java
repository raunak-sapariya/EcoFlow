package com.ecoflow.operations.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ComplaintUpdate(
		@NotBlank @Pattern(regexp = "OPEN|IN_PROGRESS|RESOLVED") String status,
		@NotBlank @Pattern(regexp = "HIGH|MEDIUM|LOW") String priority,
		@Size(max = 300) String resolutionNote) {
}
