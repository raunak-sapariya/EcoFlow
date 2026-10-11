package com.ecoflow.water.model;

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

//water_readings
@Entity
@Table(name = "water_readings",
		indexes = @Index(name = "idx_readings_source_date", columnList = "source_id, reading_date"))
public class WaterReading {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JsonIgnore // the JSON only carries source id
	@ManyToOne(optional = false)
	@JoinColumn(name = "source_id") // FK 
	private WaterSource source;

	@Column(nullable = false)
	private LocalDate readingDate;

	private Double waterLevelM;

	@Column(nullable = false)
	private double storageMl;

	private Double inflowMld; // mil lit/day

	private Double outflowMld;

	private Double rainfallMm;

	protected WaterReading() {
		
	}

	public WaterReading(WaterSource source, LocalDate readingDate, Double waterLevelM, double storageMl,
			Double inflowMld, Double outflowMld, Double rainfallMm) {
		this.source = source;
		this.readingDate = readingDate;
		this.waterLevelM = waterLevelM;
		this.storageMl = storageMl;
		this.inflowMld = inflowMld;
		this.outflowMld = outflowMld;
		this.rainfallMm = rainfallMm;
	}

	public Long getId() {
		return id;
	}

	public WaterSource getSource() {
		return source;
	}

	public Long getSourceId() {
		return source.getId();
	}

	public LocalDate getReadingDate() {
		return readingDate;
	}

	public Double getWaterLevelM() {
		return waterLevelM;
	}

	public double getStorageMl() {
		return storageMl;
	}

	public Double getInflowMld() {
		return inflowMld;
	}

	public Double getOutflowMld() {
		return outflowMld;
	}

	public Double getRainfallMm() {
		return rainfallMm;
	}

}
