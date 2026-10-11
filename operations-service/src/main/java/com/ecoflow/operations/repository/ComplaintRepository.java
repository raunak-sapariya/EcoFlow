package com.ecoflow.operations.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecoflow.operations.model.Complaint;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

	List<Complaint> findAllByOrderByCreatedAtDesc();

	List<Complaint> findByZoneIdAndStatusInOrderByCreatedAtDesc(Long zoneId, Collection<String> statuses);

}
