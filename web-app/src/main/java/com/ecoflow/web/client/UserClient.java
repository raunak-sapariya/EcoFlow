package com.ecoflow.web.client;

import java.time.Instant;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

/** Calls user-service (http://localhost:8081). User management(Create, Read, Update, Delete). */
public class UserClient extends ApiClient {

	public record User(Long id, String username, String email, String fullName, String role, Boolean enabled,
			Instant createdAt) {
	}

	public record NewUser(String username, String fullName, String email, String password, String role) {
	}

	private record LoginPayload(String username, String password) {
	}

	public UserClient(RestClient rest) {
		super("user-service", rest);
	}

	public User login(String username, String password) {
		return execute(() -> rest.post().uri("/api/auth/login")
				.body(new LoginPayload(username, password))
				.retrieve().body(User.class));
	}

	public List<User> findAll() {
		return execute(() -> rest.get().uri("/api/users").retrieve().body(new ParameterizedTypeReference<List<User>>() {}));
	}

	public void create(NewUser form) {
		executeVoid(() -> rest.post().uri("/api/users").body(form).retrieve().toBodilessEntity());
	}

	public void delete(Long id) {
		executeVoid(() -> rest.delete().uri("/api/users/{id}", id).retrieve().toBodilessEntity());
	}

}
