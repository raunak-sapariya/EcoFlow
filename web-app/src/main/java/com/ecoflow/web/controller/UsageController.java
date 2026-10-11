package com.ecoflow.web.controller;

import java.time.YearMonth;

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
@RequestMapping("/usage")
public class UsageController {

	private final SupplyClient supplyClient;

	public UsageController(SupplyClient supplyClient) {
		this.supplyClient = supplyClient;
	}

	@GetMapping
	String list(Model model) {
		model.addAttribute("usage", supplyClient.usage());
		model.addAttribute("zones", supplyClient.zones());
		model.addAttribute("lastMonth", YearMonth.now().minusMonths(1));
		return "usage";
	}

	@PostMapping
	String add(@ModelAttribute SupplyClient.UsageForm form, RedirectAttributes redirect) {
		try {
			supplyClient.addUsage(form);
			redirect.addFlashAttribute("success", "Usage saved");
		}
		catch (ApiException ex) {
			redirect.addFlashAttribute("error", ex.getMessage());
		}
		return "redirect:/usage";
	}

}
