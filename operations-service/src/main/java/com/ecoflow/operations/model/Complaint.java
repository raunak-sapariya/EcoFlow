package com.ecoflow.operations.model;

import java.time.LocalDateTime;

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

//public complaint OR a water-supply request.
//INDEX (zone_id, status): the zone page and the dashboard count "open complaints of a zone".

@Entity
@Table(name = "complaints",
		indexes = @Index(name = "idx_complaints_zone_status", columnList = "zone_id, status"))
public class Complaint {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 20)
	private String category;

	@Column(nullable = false, length = 500)
	private String description;

	@Column(nullable = false)
	private Long zoneId;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "asset_id")
	private Asset asset;

	@Column(nullable = false, length = 100)
	private String citizenName;

	@Column(nullable = false, length = 15)
	private String phone;

	@Column(length = 200)
	private String address;

	@Column(nullable = false, length = 10)
	private String priority;

	@Column(nullable = false, length = 20)
	private String status = "OPEN";

	@Column(length = 300)
	private String resolutionNote;

	@Column(nullable = false)
	private LocalDateTime createdAt = LocalDateTime.now();

	private LocalDateTime resolvedAt;

	protected Complaint() {
	}

	public Complaint(String category, String description, Long zoneId, Asset asset, String citizenName, String phone,
			String address, String priority) {
		this.category = category;
		this.description = description;
		this.zoneId = zoneId;
		this.asset = asset;
		this.citizenName = citizenName;
		this.phone = phone;
		this.address = address;
		this.priority = priority;
	}

	public Long getId() {
		return id;
	}

	public String getCategory() {
		return category;
	}

	public String getDescription() {
		return description;
	}

	public Long getZoneId() {
		return zoneId;
	}

	public Asset getAsset() {
		return asset;
	}

	public void setAsset(Asset asset) {
		this.asset = asset;
	}

	public Long getAssetId() {
		return asset == null ? null : asset.getId();
	}

	public String getAssetName() {
		return asset == null ? null : asset.getName();
	}

	public String getCitizenName() {
		return citizenName;
	}

	public String getPhone() {
		return phone;
	}

	public String getAddress() {
		return address;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getResolutionNote() {
		return resolutionNote;
	}

	public void setResolutionNote(String resolutionNote) {
		this.resolutionNote = resolutionNote;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getResolvedAt() {
		return resolvedAt;
	}

	public void setResolvedAt(LocalDateTime resolvedAt) {
		this.resolvedAt = resolvedAt;
	}

}
