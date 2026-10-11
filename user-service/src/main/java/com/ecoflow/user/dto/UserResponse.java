package com.ecoflow.user.dto;

import java.time.Instant;

import com.ecoflow.user.model.User;

/** What we send over the wire. Never expose the entity directly (it holds the password hash). */
public record UserResponse(Long id, String username, String email, String fullName, String role, boolean enabled,
		Instant createdAt) {

	public static UserResponse from(User user) {
		return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getFullName(),
				user.getRole(), user.isEnabled(), user.getCreatedAt());
	}

}
