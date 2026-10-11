package com.ecoflow.user.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.ecoflow.user.dto.CreateUserRequest;
import com.ecoflow.user.service.UserService;

/** Creates the two demo accounts on an empty database. Change the passwords before a real deployment! */
@Component
public class DataSeeder implements ApplicationRunner {

	private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

	private final UserService userService;

	public DataSeeder(UserService userService) {
		this.userService = userService;
	}

	@Override
	public void run(ApplicationArguments args) {
		if (userService.count() > 0) {
			return;
		}
		userService.create(new CreateUserRequest("admin", "admin@ecoflow.tn.gov.in", "admin123", "EcoFlow Admin", "ADMIN"));
		userService.create(new CreateUserRequest("staff", "staff@ecoflow.tn.gov.in", "staff123", "EcoFlow Staff", "STAFF"));
		log.info("Seeded demo accounts: admin/admin123 (ADMIN) and staff/staff123 (STAFF)");
	}

}
