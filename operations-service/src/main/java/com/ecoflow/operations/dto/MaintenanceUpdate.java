package com.ecoflow.operations.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record MaintenanceUpdate(@NotBlank @Pattern(regexp = "SCHEDULED|IN_PROGRESS|DONE") String status) {
}
