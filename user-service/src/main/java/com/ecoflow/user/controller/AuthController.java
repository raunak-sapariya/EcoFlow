package com.ecoflow.user.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecoflow.user.dto.LoginRequest;
import com.ecoflow.user.dto.UserResponse;
import com.ecoflow.user.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final UserService userService;

	public AuthController(UserService userService) {
		this.userService = userService;
	}

	/** Checks the credentials and returns the user. (The web-app builds its own session from this.) */
	@PostMapping("/login")
	public UserResponse login(@Valid @RequestBody LoginRequest request) {
		return UserResponse.from(userService.authenticate(request.username(), request.password()));
	}

}
