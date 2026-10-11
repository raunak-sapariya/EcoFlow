package com.ecoflow.supply.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecoflow.supply.model.SupplyZone;

public interface SupplyZoneRepository extends JpaRepository<SupplyZone, Long> {

	/** Returns zones newest first. */
	List<SupplyZone> findAllByOrderByIdDesc();

}
