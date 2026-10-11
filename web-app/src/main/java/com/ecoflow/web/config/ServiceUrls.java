package com.ecoflow.web.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.services")
public record ServiceUrls(String userService, String waterService, String supplyService, String operationsService) {
}
