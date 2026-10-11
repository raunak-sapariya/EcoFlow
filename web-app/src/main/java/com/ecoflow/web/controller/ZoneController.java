package com.ecoflow.web.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ecoflow.web.client.ApiException;
import com.ecoflow.web.client.OperationsClient;
import com.ecoflow.web.client.SupplyClient;
import com.ecoflow.web.client.WaterClient;

@Controller
@RequestMapping("/zones")
public class ZoneController {

	private final SupplyClient supplyClient;
	private final WaterClient waterClient;
	private final OperationsClient operationsClient;

	public ZoneController(SupplyClient supplyClient, WaterClient waterClient, OperationsClient operationsClient) {
		this.supplyClient = supplyClient;
		this.waterClient = waterClient;
		this.operationsClient = operationsClient;
	}

	@GetMapping
	String list(Model model) {
		List<WaterClient.Source> sources = waterClient.sources();
		Map<Long, String> sourceNames = new HashMap<>();
		for (WaterClient.Source s : sources) {
			sourceNames.put(s.id(), s.name());
		}
		model.addAttribute("zones", supplyClient.zones());
		model.addAttribute("sources", sources);
		model.addAttribute("sourceNames", sourceNames);
		return "zones";
	}

	@PostMapping
	String create(@ModelAttribute SupplyClient.ZoneForm form, RedirectAttributes redirect) {
		try {
			supplyClient.createZone(form);
			redirect.addFlashAttribute("success", "Zone '" + form.name() + "' saved");
		}
		catch (ApiException ex) {
			redirect.addFlashAttribute("error", ex.getMessage());
		}
		return "redirect:/zones";
	}

	@GetMapping("/{id}")
	String situation(@PathVariable Long id, Model model) {
		SupplyClient.Zone zone = supplyClient.zone(id);
		SupplyClient.Plan plan = supplyClient.plan(id);

		model.addAttribute("zone", zone);
		model.addAttribute("source", waterClient.source(zone.sourceId()));
		model.addAttribute("plan", plan);
		model.addAttribute("tomorrow", LocalDate.now().plusDays(1));
		model.addAttribute("schedules", supplyClient.schedulesOfZone(id));
		model.addAttribute("usage", supplyClient.usageOfZone(id));
		model.addAttribute("assets", operationsClient.assetsOfZone(id));
		model.addAttribute("tasks", operationsClient.openTasksOfZone(id));
		model.addAttribute("complaints", operationsClient.openComplaintsOfZone(id));
		return "zone-situation";
	}

	@PostMapping("/{id}/schedules")
	String planCycle(@PathVariable Long id, @ModelAttribute SupplyClient.ScheduleForm form, RedirectAttributes redirect) {
		try {
			supplyClient.createSchedule(form);
			redirect.addFlashAttribute("success", "Supply cycle saved: " + form.allocatedMl() + " ML on " + form.supplyDate());
		}
		catch (ApiException ex) {
			redirect.addFlashAttribute("error", ex.getMessage());
		}
		return "redirect:/zones/" + id;
	}

}
