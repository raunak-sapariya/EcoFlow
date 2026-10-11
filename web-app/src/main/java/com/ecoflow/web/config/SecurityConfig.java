package com.ecoflow.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Which URLs need a login, and which role.
 *   public  : /report and /track (citizens file and follow complaints without an account), login, static files
 *   ADMIN   : /users/** (staff accounts)
 *   everyone logged in (ADMIN or STAFF): all other pages
 */
@Configuration
public class SecurityConfig {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) {http.authorizeHttpRequests(auth -> auth
		.requestMatchers("/login", "/report", "/track", "/error", "/css/**", "/js/**", "/webjars/**", "/favicon.svg", "/actuator/health").permitAll()
		.requestMatchers("/users/**").hasRole("ADMIN").anyRequest().authenticated())
		.formLogin(form -> form.loginPage("/login").defaultSuccessUrl("/dashboard", true))
		.logout(logout -> logout.logoutSuccessUrl("/login?logout"));
		return http.build();
	}

}
