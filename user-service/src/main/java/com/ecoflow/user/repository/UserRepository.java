package com.ecoflow.user.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecoflow.user.model.User;

/**
 * Spring Data JPA writes the implementation for us.
 * Method names are turned into SQL: findByUsernameIgnoreCase -> WHERE lower(username) = lower(?)
 */
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUsernameIgnoreCase(String username);

	boolean existsByUsernameIgnoreCase(String username);

	/** Newest first, so a just-created account is at the top of the table. */
	List<User> findAllByOrderByIdDesc();

}
