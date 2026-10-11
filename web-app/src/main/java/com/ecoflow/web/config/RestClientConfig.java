package com.ecoflow.web.config;

import java.net.http.HttpClient;
import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import com.ecoflow.web.client.OperationsClient;
import com.ecoflow.web.client.SupplyClient;
import com.ecoflow.web.client.UserClient;
import com.ecoflow.web.client.WaterClient;

/*
	Beans for all the RestClients used to call other services. Each client has a base URL and a same timeout.
*/
@Configuration
public class RestClientConfig {

	@Bean
	UserClient userClient(ServiceUrls urls) {
		return new UserClient(create(urls.userService()));
	}

	@Bean
	WaterClient waterClient(ServiceUrls urls) {
		return new WaterClient(create(urls.waterService()));
	}

	@Bean
	SupplyClient supplyClient(ServiceUrls urls) {
		return new SupplyClient(create(urls.supplyService()));
	}

	@Bean
	OperationsClient operationsClient(ServiceUrls urls) {
		return new OperationsClient(create(urls.operationsService()));
	}

	private static RestClient create(String baseUrl) {
		HttpClient httpClient = HttpClient.newBuilder()
				.version(HttpClient.Version.HTTP_1_1)
				.connectTimeout(Duration.ofSeconds(2))
				.build();
		JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(httpClient);
		requestFactory.setReadTimeout(Duration.ofSeconds(5));
		return RestClient.builder().baseUrl(baseUrl).requestFactory(requestFactory).build();
	}

}
