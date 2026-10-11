package com.ecoflow.supply.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// maps water distribution zone to the supply_zones table in supply_db

@Entity
@Table(name = "supply_zones")
public class SupplyZone {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 100)
	private String name;

	@Column(nullable = false, length = 60)
	private String city;

	@Column(nullable = false)
	private int population;

	@Column(nullable = false)
	private double dailyDemandMl; // million litres per day

	@Column(nullable = false)
	private Long sourceId; // -> water-service

	@Column(nullable = false, length = 20)
	private String status = "ACTIVE"; // ACTIVE | INACTIVE

	protected SupplyZone() {
		// required by JPA
	}

	public SupplyZone(String name, String city, int population, double dailyDemandMl, Long sourceId) {
		this.name = name;
		this.city = city;
		this.population = population;
		this.dailyDemandMl = dailyDemandMl;
		this.sourceId = sourceId;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getPopulation() {
		return population;
	}

	public void setPopulation(int population) {
		this.population = population;
	}

	public double getDailyDemandMl() {
		return dailyDemandMl;
	}

	public void setDailyDemandMl(double dailyDemandMl) {
		this.dailyDemandMl = dailyDemandMl;
	}

	public Long getSourceId() {
		return sourceId;
	}

	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
