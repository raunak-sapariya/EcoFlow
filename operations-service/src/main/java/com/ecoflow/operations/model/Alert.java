package com.ecoflow.operations.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** alert written by a staff member through the Alerts form.*/
@Entity
@Table(name = "alerts")
public class Alert {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 120)
	private String title;

	@Column(nullable = false, length = 500)
	private String message;

	@Column(nullable = false, length = 10)
	private String severity;

	@Column(length = 100)
	private String createdBy;

	@Column(nullable = false)
	private LocalDateTime createdAt = LocalDateTime.now();

	protected Alert() {
	}

	public Alert(String title, String message, String severity, String createdBy) {
		this.title = title;
		this.message = message;
		this.severity = severity;
		this.createdBy = createdBy;
	}

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getMessage() {
		return message;
	}

	public String getSeverity() {
		return severity;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

}
