package com.ecoflow.web.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.ecoflow.web.client.OperationsClient;
import com.ecoflow.web.client.ServiceUnavailableException;
import com.ecoflow.web.client.SupplyClient;
import com.ecoflow.web.client.WaterClient;

@Controller
public class DashboardController {

	private final WaterClient waterClient;
	private final SupplyClient supplyClient;
	private final OperationsClient operationsClient;

	public DashboardController(WaterClient waterClient, SupplyClient supplyClient, OperationsClient operationsClient) {
		this.waterClient = waterClient;
		this.supplyClient = supplyClient;
		this.operationsClient = operationsClient;
	}

	@GetMapping("/dashboard")
	String dashboard(Model model) {
		List<OperationsClient.Alert> alerts = callOrNull(operationsClient::alerts);
		model.addAttribute("alerts", alerts == null ? List.of() : alerts.stream().limit(5).toList());

		model.addAttribute("water", callOrNull(waterClient::summary));
		List<WaterClient.Source> sources = callOrNull(waterClient::sources);
		if (sources != null) {
			Map<String, Integer> availability = counters("NORMAL", "WATCH", "CRITICAL");
			Map<String, Integer> quality = counters("PASS", "FAIL", "NOT TESTED");
			for (WaterClient.Source s : sources) {
				addOne(availability, s.availability());
				addOne(quality, s.lastQualityResult() == null ? "NOT TESTED" : s.lastQualityResult());
			}
			model.addAttribute("sourcesByAvailability", availability);
			model.addAttribute("sourcesByQuality", quality);
		}

		List<SupplyClient.Zone> zones = callOrNull(supplyClient::zones);
		if (zones != null) {
			List<String> zoneNames = new ArrayList<>();
			List<Double> zoneDemand = new ArrayList<>();
			for (SupplyClient.Zone z : zones) {
				zoneNames.add(z.name());
				zoneDemand.add(z.dailyDemandMl());
			}
			model.addAttribute("zoneNames", zoneNames);
			model.addAttribute("zoneDemand", zoneDemand);
		}
		SupplyClient.Summary supply = callOrNull(supplyClient::summary);
		model.addAttribute("supply", supply);
		if (supply != null) {
			double usageTotal = 0;
			for (Double volume : supply.usageByCategory().values()) {
				usageTotal += volume;
			}
			model.addAttribute("usageTotal", Math.round(usageTotal));
			List<String> days = new ArrayList<>();
			List<Double> planned = new ArrayList<>();
			List<Double> supplied = new ArrayList<>();
			for (SupplyClient.DailySupply d : supply.lastSevenDays()) {
				days.add(d.date().getDayOfMonth() + "/" + d.date().getMonthValue());
				planned.add(d.plannedMl());
				supplied.add(d.suppliedMl());
			}
			model.addAttribute("supplyDays", days);
			model.addAttribute("supplyPlanned", planned);
			model.addAttribute("supplySupplied", supplied);
		}

		model.addAttribute("ops", callOrNull(operationsClient::summary));
		List<OperationsClient.Asset> assets = callOrNull(operationsClient::assets);
		if (assets != null) {
			Map<String, Integer> assetStatus = counters("WORKING", "FAULTY", "UNDER_REPAIR");
			for (OperationsClient.Asset a : assets) {
				addOne(assetStatus, a.status());
			}
			model.addAttribute("assetsByStatus", assetStatus);
		}
		List<OperationsClient.Task> tasks = callOrNull(operationsClient::tasks);
		if (tasks != null) {
			Map<String, Integer> taskStatus = counters("SCHEDULED", "IN_PROGRESS", "DONE");
			for (OperationsClient.Task t : tasks) {
				addOne(taskStatus, t.status());
			}
			model.addAttribute("tasksByStatus", taskStatus);
		}
		List<OperationsClient.Complaint> complaints = callOrNull(operationsClient::complaints);
		if (complaints != null) {
			Map<String, Integer> openByCategory = counters("LEAKAGE", "NO_SUPPLY", "LOW_PRESSURE", "QUALITY",
					"SUPPLY_REQUEST", "OTHER");
			for (OperationsClient.Complaint c : complaints) {
				if (!"RESOLVED".equals(c.status())) {
					addOne(openByCategory, c.category());
				}
			}
			model.addAttribute("openComplaintsByCategory", openByCategory);
		}
		return "dashboard";
	}

	private static Map<String, Integer> counters(String... keys) {
		Map<String, Integer> counts = new LinkedHashMap<>();
		for (String key : keys) {
			counts.put(key, 0);
		}
		return counts;
	}

	private static void addOne(Map<String, Integer> counts, String key) {
		counts.put(key, counts.getOrDefault(key, 0) + 1);
	}

	private static <T> T callOrNull(Supplier<T> call) {
		try {
			return call.get();
		}
		catch (ServiceUnavailableException ex) {
			return null;
		}
	}

}
