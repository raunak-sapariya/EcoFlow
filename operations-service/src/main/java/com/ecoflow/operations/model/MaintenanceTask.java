package com.ecoflow.operations.model;

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

//Planned upkeep (PREVENTIVE) or a fault being fixed (REPAIR). Table: maintenance_tasks.
//the alert cron job asks status every 5 minutes.

@Entity
@Table(name = "maintenance_tasks",
		indexes = @Index(name = "idx_maintenance_status_date", columnList = "status, scheduled_date"))
public class MaintenanceTask {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JsonIgnore
	@ManyToOne(optional = false)
	@JoinColumn(name = "asset_id")
	private Asset asset;

	@Column(nullable = false, length = 20)
	private String type;

	@Column(nullable = false, length = 300)
	private String description;

	@Column(nullable = false)
	private LocalDate scheduledDate;

	private LocalDate completedDate;

	@Column(length = 100)
	private String technician;

	private Double cost;

	@Column(nullable = false, length = 20)
	private String status = "SCHEDULED";
	protected MaintenanceTask() {
	}

	public MaintenanceTask(Asset asset, String type, String description, LocalDate scheduledDate, String technician,
			Double cost) {
		this.asset = asset;
		this.type = type;
		this.description = description;
		this.scheduledDate = scheduledDate;
		this.technician = technician;
		this.cost = cost;
	}

	//not finished and its date has passed.
	public boolean isOverdue() {
		return !"DONE".equals(status) && scheduledDate.isBefore(LocalDate.now());
	}

	public Long getId() {
		return id;
	}

	public Asset getAsset() {
		return asset;
	}

	public Long getAssetId() {
		return asset.getId();
	}

	public String getAssetName() {
		return asset.getName();
	}

	public Long getZoneId() {
		return asset.getZoneId();
	}

	public String getType() {
		return type;
	}

	public String getDescription() {
		return description;
	}

	public LocalDate getScheduledDate() {
		return scheduledDate;
	}

	public LocalDate getCompletedDate() {
		return completedDate;
	}

	public void setCompletedDate(LocalDate completedDate) {
		this.completedDate = completedDate;
	}

	public String getTechnician() {
		return technician;
	}

	public void setTechnician(String technician) {
		this.technician = technician;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
