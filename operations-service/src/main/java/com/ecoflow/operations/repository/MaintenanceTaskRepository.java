package com.ecoflow.operations.repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecoflow.operations.model.MaintenanceTask;

public interface MaintenanceTaskRepository extends JpaRepository<MaintenanceTask, Long> {

	List<MaintenanceTask> findAllByOrderByIdDesc();

	List<MaintenanceTask> findByAsset_ZoneIdOrderByIdDesc(Long zoneId);

	long countByStatusInAndScheduledDateBefore(Collection<String> statuses, LocalDate date);

}
