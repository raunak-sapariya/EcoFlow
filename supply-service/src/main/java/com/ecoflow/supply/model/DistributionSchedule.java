package com.ecoflow.supply.model;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

// Represents one water distribution cycle and maps it to the distribution_schedules table in the supply_db database.

@Entity
@Table(name = "distribution_schedules",
		indexes = @Index(name = "idx_schedules_zone_date", columnList = "zone_id, supply_date"))
public class DistributionSchedule {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JsonIgnore
	@ManyToOne(optional = false)
	@JoinColumn(name = "zone_id")
	private SupplyZone zone;

	@Column(nullable = false)
	private LocalDate supplyDate;

	@Column(nullable = false)
	private LocalTime startTime;

	@Column(nullable = false)
	private LocalTime endTime;

	@Column(nullable = false)
	private double allocatedMl;

	private Double suppliedMl; 

	@Column(nullable = false, length = 20)
	private String status = "PLANNED"; 

	protected DistributionSchedule() {
		
	}

	public DistributionSchedule(SupplyZone zone, LocalDate supplyDate, LocalTime startTime, LocalTime endTime,
			double allocatedMl) {
		this.zone = zone;
		this.supplyDate = supplyDate;
		this.startTime = startTime;
		this.endTime = endTime;
		this.allocatedMl = allocatedMl;
	}

	public Long getId() {
		return id;
	}

	public Long getZoneId() {
		return zone.getId();
	}

	public String getZoneName() {
		return zone.getName();
	}

	public LocalDate getSupplyDate() {
		return supplyDate;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public double getAllocatedMl() {
		return allocatedMl;
	}

	public Double getSuppliedMl() {
		return suppliedMl;
	}

	public String getStatus() {
		return status;
	}

	public void complete(double suppliedMl) {
		this.suppliedMl = suppliedMl;
		this.status = "COMPLETED";
	}

	public void cancel() {
		this.status = "CANCELLED";
	}

}
