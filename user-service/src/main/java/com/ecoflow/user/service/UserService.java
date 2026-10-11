package com.ecoflow.user.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.ecoflow.user.dto.CreateUserRequest;
import com.ecoflow.user.model.User;
import com.ecoflow.user.repository.UserRepository;

/** Business logic lives here - controllers stay thin, repositories stay dumb. */
@Service
@Transactional
public class UserService {

	private final UserRepository users;
	private final PasswordEncoder passwordEncoder;

	public UserService(UserRepository users, PasswordEncoder passwordEncoder) {
		this.users = users;
		this.passwordEncoder = passwordEncoder;
	}

	public User create(CreateUserRequest request) {
		if (users.existsByUsernameIgnoreCase(request.username())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Username '" + request.username() + "' is already taken");
		}
		User user = new User(request.username(), request.email(), passwordEncoder.encode(request.password()),
				request.fullName(), request.role());
		return users.save(user);
	}

	/** Checks username + password. Same error for "no such user" and "wrong password" so usernames can't be probed. */
	@Transactional(readOnly = true)
	public User authenticate(String username, String password) {
		User user = users.findByUsernameIgnoreCase(username)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password"));
		if (!passwordEncoder.matches(password, user.getPasswordHash())) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
		}
		if (!user.isEnabled()) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This account is disabled");
		}
		return user;
	}

	@Transactional(readOnly = true)
	public List<User> findAll() {
		return users.findAllByOrderByIdDesc();
	}

	@Transactional(readOnly = true)
	public User findById(Long id) {
		return users.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User " + id + " not found"));
	}

	public void delete(Long id) {
		users.delete(findById(id));
	}

	@Transactional(readOnly = true)
	public long count() {
		return users.count();
	}

}
