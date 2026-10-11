package com.ecoflow.operations.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecoflow.operations.model.Asset;

public interface AssetRepository extends JpaRepository<Asset, Long> {

	List<Asset> findAllByOrderByIdDesc();

	List<Asset> findByZoneIdOrderByIdDesc(Long zoneId);

}
