package com.ecoflow.supply.config;

import java.net.http.HttpClient;
import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import com.ecoflow.supply.client.WaterClient;

@Configuration
public class RestClientConfig {

	@Bean
	WaterClient waterClient(ServiceUrls urls) {
		// Always set timeouts: a hung service must not hang us too.
		HttpClient httpClient = HttpClient.newBuilder()
				.version(HttpClient.Version.HTTP_1_1)
				.connectTimeout(Duration.ofSeconds(2))
				.build();
		JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(httpClient);
		requestFactory.setReadTimeout(Duration.ofSeconds(5));
		return new WaterClient(RestClient.builder().baseUrl(urls.waterService()).requestFactory(requestFactory).build());
	}

}
