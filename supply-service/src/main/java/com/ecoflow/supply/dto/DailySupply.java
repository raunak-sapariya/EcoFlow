package com.ecoflow.supply.dto;

import java.time.LocalDate;

// Represents the total planned vs actual water supply across all zones for a single day

public record DailySupply(LocalDate date, double plannedMl, double suppliedMl) {
}
