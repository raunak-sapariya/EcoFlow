package com.ecoflow.web.controller;

import java.time.LocalDate;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ecoflow.web.client.ApiException;
import com.ecoflow.web.client.SupplyClient;

@Controller
@RequestMapping("/schedules")
public class ScheduleController {

	private final SupplyClient supplyClient;

	public ScheduleController(SupplyClient supplyClient) {
		this.supplyClient = supplyClient;
	}

	@GetMapping
	String list(Model model) {
		model.addAttribute("schedules", supplyClient.schedules());
		model.addAttribute("zones", supplyClient.zones());
		model.addAttribute("tomorrow", LocalDate.now().plusDays(1));
		return "schedules";
	}

	@PostMapping
	String create(@ModelAttribute SupplyClient.ScheduleForm form, RedirectAttributes redirect) {
		try {
			supplyClient.createSchedule(form);
			redirect.addFlashAttribute("success", "Supply cycle saved");
		}
		catch (ApiException ex) {
			redirect.addFlashAttribute("error", ex.getMessage());
		}
		return "redirect:/schedules";
	}

}
