// Allows supply-service to communicate with water-service and retrieve the current status of a water source.

package com.ecoflow.supply.client;

import java.time.LocalDate;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;


public class WaterClient {

	//The fields of water-service's SourceStatus that supply-service needs
	public record Source(Long id, String name, Double storageMl, Double storagePercent, String availability,
			LocalDate lastReadingDate) {
	}

	private final RestClient rest;

	public WaterClient(RestClient rest) {
		this.rest = rest;
	}

	// 400 if it does not exist, 503 if water-service is down. */
	public Source getSource(Long id) {
		try {
			return rest.get().uri("/api/sources/{id}", id).retrieve().body(Source.class);
		}
		catch (HttpClientErrorException.NotFound ex) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Water source " + id + " does not exist");
		}
		catch (RestClientException ex) { // connection refused, timeout
			throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
					"water-service is not reachable, so the source's water level cannot be checked");
		}
	}

}
