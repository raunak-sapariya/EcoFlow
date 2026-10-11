package com.ecoflow.user.model;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Model layer (M in MVC): maps to the "users" table.
 * The table is called "users" because "user" is a reserved word in PostgreSQL.
 */
@Entity
@Table(name = "users")
public class User {

	public static final String ADMIN = "ADMIN"; // manages staff accounts + everything else
	public static final String STAFF = "STAFF"; // water authority officer: uses all operational pages

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true, length = 50) // unique -> PostgreSQL creates an index for login lookups
	private String username;

	@Column(nullable = false, length = 150)
	private String email;

	@Column(nullable = false)
	private String passwordHash;

	@Column(nullable = false, length = 100)
	private String fullName;

	@Column(nullable = false, length = 20)
	private String role = STAFF;

	@Column(nullable = false)
	private boolean enabled = true;

	@Column(nullable = false, updatable = false)
	private Instant createdAt = Instant.now();

	protected User() {
		// required by JPA
	}

	public User(String username, String email, String passwordHash, String fullName, String role) {
		this.username = username;
		this.email = email;
		this.passwordHash = passwordHash;
		this.fullName = fullName;
		this.role = role;
	}

	public Long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getEmail() {
		return email;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public String getFullName() {
		return fullName;
	}

	public String getRole() {
		return role;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

}
