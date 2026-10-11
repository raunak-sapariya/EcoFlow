package com.ecoflow.web.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.ecoflow.web.client.ApiClient;
import com.ecoflow.web.client.OperationsClient;
import com.ecoflow.web.client.SupplyClient;
import com.ecoflow.web.client.UserClient;
import com.ecoflow.web.client.WaterClient;
import com.ecoflow.web.config.ServiceUrls;

@Controller
public class HealthController {

	public record ServiceHealth(String name, String url, String manages, boolean up, long responseMs) {
	}

	private final UserClient userClient;
	private final WaterClient waterClient;
	private final SupplyClient supplyClient;
	private final OperationsClient operationsClient;
	private final ServiceUrls urls;

	public HealthController(UserClient userClient, WaterClient waterClient, SupplyClient supplyClient,
			OperationsClient operationsClient, ServiceUrls urls) {
		this.userClient = userClient;
		this.waterClient = waterClient;
		this.supplyClient = supplyClient;
		this.operationsClient = operationsClient;
		this.urls = urls;
	}

	@GetMapping("/health")
	String health(Model model) {
		List<ServiceHealth> services = new ArrayList<>();
		services.add(check(userClient, urls.userService(), "Staff accounts, login"));
		services.add(check(waterClient, urls.waterService(), "Water sources, level readings, quality tests"));
		services.add(check(supplyClient, urls.supplyService(), "Supply zones, water usage, distribution schedules"));
		services.add(check(operationsClient, urls.operationsService(), "Assets, maintenance, complaints, alerts"));

		int running = 0;
		for (ServiceHealth s : services) {
			if (s.up()) {
				running++;
			}
		}
		model.addAttribute("services", services);
		model.addAttribute("running", running);
		model.addAttribute("checkedAt", LocalDateTime.now());
		return "health";
	}

	private static ServiceHealth check(ApiClient client, String url, String manages) {
		long start = System.currentTimeMillis();
		boolean up = client.isUp();
		long took = System.currentTimeMillis() - start;
		return new ServiceHealth(client.getServiceName(), url, manages, up, took);
	}

}
