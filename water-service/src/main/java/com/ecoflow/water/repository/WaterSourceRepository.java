package com.ecoflow.water.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecoflow.water.model.WaterSource;

public interface WaterSourceRepository extends JpaRepository<WaterSource, Long> {


	List<WaterSource> findAllByOrderByIdDesc();

}
