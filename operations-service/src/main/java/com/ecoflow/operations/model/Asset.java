package com.ecoflow.operations.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

//piece of infrastructure: pump, pipeline, treatment plant, tank or valve.
//zoneId (supply-service) and sourceId (water-service) are ids in other services' databases, so they are plain columns, not foreign keys.
@Entity
@Table(name = "assets")
public class Asset {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 120)
	private String name;

	@Column(nullable = false, length = 20)
	private String type; 

	@Column(nullable = false)
	private Long zoneId;

	private Long sourceId;

	private LocalDate installDate;

	@Column(nullable = false, length = 10)
	private String condition = "GOOD";
	@Column(nullable = false, length = 20)
	private String status = "WORKING";

	protected Asset() {
	}

	public Asset(String name, String type, Long zoneId, Long sourceId, LocalDate installDate, String condition,
			String status) {
		this.name = name;
		this.type = type;
		this.zoneId = zoneId;
		this.sourceId = sourceId;
		this.installDate = installDate;
		this.condition = condition;
		this.status = status;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getZoneId() {
		return zoneId;
	}

	public void setZoneId(Long zoneId) {
		this.zoneId = zoneId;
	}

	public Long getSourceId() {
		return sourceId;
	}

	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}

	public LocalDate getInstallDate() {
		return installDate;
	}

	public void setInstallDate(LocalDate installDate) {
		this.installDate = installDate;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
