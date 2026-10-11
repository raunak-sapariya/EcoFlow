package com.ecoflow.supply.model;

import java.time.LocalDate;

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

/* Represents a record of water consumed in a particular zone, during a particular month, by a specific category of user. 
   It maps to the usage_records table in supply_db
 */ 

@Entity
@Table(name = "usage_records",
		indexes = @Index(name = "idx_usage_zone_month", columnList = "zone_id, usage_month"))
public class UsageRecord {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JsonIgnore
	@ManyToOne(optional = false)
	@JoinColumn(name = "zone_id") 
	private SupplyZone zone;

	@Column(nullable = false)
	private LocalDate usageMonth; 

	@Column(nullable = false, length = 20)
	private String category; 

	@Column(nullable = false)
	private double volumeMl;

	protected UsageRecord() {
		
	}

	public UsageRecord(SupplyZone zone, LocalDate usageMonth, String category, double volumeMl) {
		this.zone = zone;
		this.usageMonth = usageMonth;
		this.category = category;
		this.volumeMl = volumeMl;
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

	public LocalDate getUsageMonth() {
		return usageMonth;
	}

	public String getCategory() {
		return category;
	}

	public double getVolumeMl() {
		return volumeMl;
	}

}
