package com.ecoflow.water.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

//water_sources
@Entity
@Table(name = "water_sources")
public class WaterSource {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 100)
	private String name;

	@Column(nullable = false, length = 20)
	private String type;

	@Column(nullable = false, length = 60)
	private String district;

	private Double latitude;

	private Double longitude;

	@Column(nullable = false)
	private double capacityMl;

	@Column(length = 100)
	private String managedBy; 

	@Column(nullable = false, length = 20)
	private String status = "ACTIVE"; 

	protected WaterSource() {
		
	}

	public WaterSource(String name, String type, String district, Double latitude, Double longitude, double capacityMl,
			String managedBy) {
		this.name = name;
		this.type = type;
		this.district = district;
		this.latitude = latitude;
		this.longitude = longitude;
		this.capacityMl = capacityMl;
		this.managedBy = managedBy;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public String getDistrict() {
		return district;
	}

	public Double getLatitude() {
		return latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public double getCapacityMl() {
		return capacityMl;
	}

	public String getManagedBy() {
		return managedBy;
	}

	public String getStatus() {
		return status;
	}

}
