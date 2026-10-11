package com.ecoflow.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/** JSON body of POST /api/users (the admin creates staff accounts; there is no self-registration). */
public record CreateUserRequest(
		@NotBlank @Size(min = 3, max = 50)
		@Pattern(regexp = "[A-Za-z0-9_.-]+", message = "may only contain letters, digits, '_', '.' and '-'")
		String username,
		@NotBlank @Email @Size(max = 150) String email,
		@NotBlank @Size(min = 6, max = 100) String password,
		@NotBlank @Size(max = 100) String fullName,
		@NotBlank @Pattern(regexp = "ADMIN|STAFF") String role) {
}
