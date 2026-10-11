package com.ecoflow.web.security;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.ecoflow.web.client.UserClient;

/**
 * The logged-in user, stored in the HTTP session.
 * Extends Spring Security's User so it works everywhere, and adds the extra fields our pages need.
 */
public class AppUserPrincipal extends User {

	private final Long id;
	private final String fullName;
	private final String role;

	public AppUserPrincipal(UserClient.User user) {
		super(user.username(), "", Boolean.TRUE.equals(user.enabled()), true, true, true,
				List.of(new SimpleGrantedAuthority("ROLE_" + user.role())));
		this.id = user.id();
		this.fullName = user.fullName();
		this.role = user.role();
	}

	public Long getId() {
		return id;
	}

	public String getFullName() {
		return fullName;
	}

	public String getRole() {
		return role;
	}

	public boolean isAdmin() {
		return "ADMIN".equals(role);
	}

}
