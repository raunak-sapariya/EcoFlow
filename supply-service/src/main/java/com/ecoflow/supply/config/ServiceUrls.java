package com.ecoflow.supply.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.services")
public record ServiceUrls(String waterService) {
}
