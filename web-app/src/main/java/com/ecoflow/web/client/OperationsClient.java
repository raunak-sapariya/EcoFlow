package com.ecoflow.web.client;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

/** Calls operations-service (http://localhost:8084): assets, maintenance, complaints/requests, alerts. */
public class OperationsClient extends ApiClient {

	public record Asset(Long id, String name, String type, Long zoneId, LocalDate installDate, String condition,
			String status) {
	}

	public record Task(Long id, String assetName, String type, String description, LocalDate scheduledDate,
			LocalDate completedDate, String technician, Double cost, String status) {
	}

	public record Complaint(Long id, String category, String description, Long zoneId, String assetName,
			String citizenName, String phone, String address, String priority, String status, String resolutionNote,
			LocalDateTime createdAt, LocalDateTime resolvedAt) {
	}

	public record Alert(Long id, String title, String message, String severity, String createdBy,
			LocalDateTime createdAt) {
	}

	public record Summary(Long assetCount, Long assetsNotWorking, Long maintenanceOpen, Long maintenanceOverdue,
			Long complaintsOpen, Long supplyRequestsOpen, Long alertCount) {
	}

	public record AssetForm(String name, String type, Long zoneId, LocalDate installDate, String condition,
			String status) {
	}

	public record TaskForm(Long assetId, String type, String description, LocalDate scheduledDate, String technician,
			Double cost) {
	}

	public record ComplaintForm(String category, String description, Long zoneId, String citizenName, String phone,
			String address) {
	}

	public record ComplaintUpdate(String status, String priority, String resolutionNote) {
	}

	public record AlertForm(String title, String message, String severity, String createdBy) {
	}

	private record StatusPayload(String status) {
	}

	public OperationsClient(RestClient rest) {
		super("operations-service", rest);
	}

	public List<Asset> assets() {
		return execute(() -> rest.get().uri("/api/assets").retrieve().body(new ParameterizedTypeReference<List<Asset>>() {}));
	}

	public List<Asset> assetsOfZone(Long zoneId) {
		return execute(() -> rest.get().uri("/api/assets?zoneId={id}", zoneId).retrieve().body(new ParameterizedTypeReference<List<Asset>>() {}));
	}

	public void createAsset(AssetForm form) {
		executeVoid(() -> rest.post().uri("/api/assets").body(form).retrieve().toBodilessEntity());
	}

	public List<Task> tasks() {
		return execute(() -> rest.get().uri("/api/maintenance").retrieve().body(new ParameterizedTypeReference<List<Task>>() {}));
	}

	public List<Task> openTasksOfZone(Long zoneId) {
		return execute(() -> rest.get().uri("/api/maintenance?zoneId={id}&openOnly=true", zoneId).retrieve().body(new ParameterizedTypeReference<List<Task>>() {}));
	}

	public void createTask(TaskForm form) {
		executeVoid(() -> rest.post().uri("/api/maintenance").body(form).retrieve().toBodilessEntity());
	}

	public void markTaskDone(Long id) {
		executeVoid(() -> rest.put().uri("/api/maintenance/{id}", id).body(new StatusPayload("DONE")).retrieve().toBodilessEntity());
	}

	public List<Complaint> complaints() {
		return execute(() -> rest.get().uri("/api/complaints").retrieve().body(new ParameterizedTypeReference<List<Complaint>>() {}));
	}

	public List<Complaint> openComplaintsOfZone(Long zoneId) {
		return execute(() -> rest.get().uri("/api/complaints?zoneId={id}", zoneId).retrieve().body(new ParameterizedTypeReference<List<Complaint>>() {}));
	}

	public Complaint complaint(Long id) {
		return execute(() -> rest.get().uri("/api/complaints/{id}", id).retrieve().body(Complaint.class));
	}

	public Complaint track(Long id, String phone) {
		return execute(() -> rest.get().uri("/api/complaints/track?id={id}&phone={phone}", id, phone).retrieve().body(Complaint.class));
	}

	public Complaint createComplaint(ComplaintForm form) {
		return execute(() -> rest.post().uri("/api/complaints").body(form).retrieve().body(Complaint.class));
	}

	public void updateComplaint(Long id, ComplaintUpdate form) {
		executeVoid(() -> rest.put().uri("/api/complaints/{id}", id).body(form).retrieve().toBodilessEntity());
	}

	public List<Alert> alerts() {
		return execute(() -> rest.get().uri("/api/alerts").retrieve().body(new ParameterizedTypeReference<List<Alert>>() {}));
	}

	public void createAlert(AlertForm form) {
		executeVoid(() -> rest.post().uri("/api/alerts").body(form).retrieve().toBodilessEntity());
	}

	public Summary summary() {
		return execute(() -> rest.get().uri("/api/summary").retrieve().body(Summary.class));
	}

}
