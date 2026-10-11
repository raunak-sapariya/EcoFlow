package com.ecoflow.water.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecoflow.water.model.QualityTest;

public interface QualityTestRepository extends JpaRepository<QualityTest, Long> {

	
	Optional<QualityTest> findFirstBySource_IdOrderByTestedOnDescIdDesc(Long sourceId);

	//save in top of tabel
	List<QualityTest> findAllByOrderByIdDesc();

}
