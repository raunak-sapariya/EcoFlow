package com.ecoflow.operations.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecoflow.operations.model.Alert;

public interface AlertRepository extends JpaRepository<Alert, Long> {

	List<Alert> findAllByOrderByIdDesc();

}
