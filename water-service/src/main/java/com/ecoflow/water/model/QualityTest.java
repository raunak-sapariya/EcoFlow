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


//equality_tests

@Entity
@Table(name = "quality_tests",
		indexes = @Index(name = "idx_quality_source_date", columnList = "source_id, tested_on"))
public class QualityTest {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JsonIgnore
	@ManyToOne(optional = false)
	@JoinColumn(name = "source_id") // FK 
	private WaterSource source;

	@Column(nullable = false)
	private LocalDate testedOn;

	@Column(nullable = false)
	private double ph;

	@Column(nullable = false)
	private double dissolvedOxygen; // mg/L

	@Column(nullable = false)
	private double bod; // oxygen demand  mg/L

	private Double turbidity; // nephelometric nurbidity unit

	private Double tds; // total dissolved solids - mg/L

	private Double totalColiform; // MPN/100 mL

	@Column(length = 100)
	private String labName;

	@Column(nullable = false, length = 10)
	private String result; 

	@Column(length = 300)
	private String remarks;

	protected QualityTest() {
		
	}

	public QualityTest(WaterSource source, LocalDate testedOn, double ph, double dissolvedOxygen, double bod,
			Double turbidity, Double tds, Double totalColiform, String labName, String result, String remarks) {
		this.source = source;
		this.testedOn = testedOn;
		this.ph = ph;
		this.dissolvedOxygen = dissolvedOxygen;
		this.bod = bod;
		this.turbidity = turbidity;
		this.tds = tds;
		this.totalColiform = totalColiform;
		this.labName = labName;
		this.result = result;
		this.remarks = remarks;
	}

	public Long getId() {
		return id;
	}

	public Long getSourceId() {
		return source.getId();
	}

	public String getSourceName() {
		return source.getName();
	}

	public LocalDate getTestedOn() {
		return testedOn;
	}

	public double getPh() {
		return ph;
	}

	public double getDissolvedOxygen() {
		return dissolvedOxygen;
	}

	public double getBod() {
		return bod;
	}

	public Double getTurbidity() {
		return turbidity;
	}

	public Double getTds() {
		return tds;
	}

	public Double getTotalColiform() {
		return totalColiform;
	}

	public String getLabName() {
		return labName;
	}

	public String getResult() {
		return result;
	}

	public String getRemarks() {
		return remarks;
	}

}
