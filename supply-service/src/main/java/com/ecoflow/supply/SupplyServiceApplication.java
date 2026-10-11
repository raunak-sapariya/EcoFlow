package com.ecoflow.supply;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SupplyServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SupplyServiceApplication.class, args);
	}

}
