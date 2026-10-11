package com.ecoflow.web.client;

import java.util.function.Supplier;

import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

public abstract class ApiClient {

	record ApiError(String detail) {
	}

	protected final RestClient rest;
	private final String serviceName;

	protected ApiClient(String serviceName, RestClient rest) {
		this.serviceName = serviceName;
		this.rest = rest;
	}

	public String getServiceName() {
		return serviceName;
	}

	public boolean isUp() {
		try {
			rest.get().uri("/actuator/health").retrieve().toBodilessEntity();
			return true;
		}
		catch (RestClientException ex) {
			return false;
		}
	}

	protected <T> T execute(Supplier<T> call) {
		try {
			return call.get();
		}
		catch (RestClientResponseException ex) {
			if (ex.getStatusCode().is5xxServerError()) {
				throw new ServiceUnavailableException(serviceName + " failed (HTTP " + ex.getStatusCode().value() + ")", ex);
			}
			throw new ApiException(ex.getStatusCode().value(), detailOf(ex));
		}
		catch (ResourceAccessException ex) {
			throw new ServiceUnavailableException(serviceName + " is not reachable", ex);
		}
	}

	protected void executeVoid(Runnable call) {
		execute(() -> {
			call.run();
			return null;
		});
	}

	private String detailOf(RestClientResponseException ex) {
		try {
			ApiError error = ex.getResponseBodyAs(ApiError.class);
			if (error != null && error.detail() != null) {
				return error.detail();
			}
		}
		catch (RuntimeException ignored) {
		}
		return ex.getStatusText();
	}

}
