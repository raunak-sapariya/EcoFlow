package com.ecoflow.supply.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecoflow.supply.model.UsageRecord;

// for retrieving and saving water usage records in the usage_records table

public interface UsageRecordRepository extends JpaRepository<UsageRecord, Long> {

	// The underscore in Zone_Id traverses the related entity to filter by zone.id.
	List<UsageRecord> findTop50ByOrderByIdDesc();

	/** Latest 12 usage records for one zone. */
	List<UsageRecord> findTop12ByZone_IdOrderByIdDesc(Long zoneId);

	List<UsageRecord> findByUsageMonth(LocalDate month);

}
