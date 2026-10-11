package com.ecoflow.water.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecoflow.water.model.WaterReading;


public interface WaterReadingRepository extends JpaRepository<WaterReading, Long> {

	


	Optional<WaterReading> findFirstBySource_IdOrderByReadingDateDescIdDesc(Long sourceId);


	List<WaterReading> findTop30BySource_IdOrderByIdDesc(Long sourceId);

}
