package com.ecoflow.supply.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ecoflow.supply.model.DistributionSchedule;

// Provides database operations for retrieving and saving distribution schedules

public interface DistributionScheduleRepository extends JpaRepository<DistributionSchedule, Long> {

	// The underscore in Zone_Id traverses the related entity to filter by zone.id.
	// Spring Data derives these queries from their method names.
	List<DistributionSchedule> findTop50ByOrderByIdDesc();

	// Latest 10 cycles for one zone.
	List<DistributionSchedule> findTop10ByZone_IdOrderByIdDesc(Long zoneId);

	// Cycles for dashboard summaries and charts.
	List<DistributionSchedule> findBySupplyDate(LocalDate date);

	List<DistributionSchedule> findBySupplyDateBetween(LocalDate from, LocalDate to);

}
