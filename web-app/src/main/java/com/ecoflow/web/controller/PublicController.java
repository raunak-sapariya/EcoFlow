package com.ecoflow.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ecoflow.web.client.ApiException;
import com.ecoflow.web.client.OperationsClient;
import com.ecoflow.web.client.SupplyClient;

@Controller
public class PublicController {

	private final OperationsClient operationsClient;
	private final SupplyClient supplyClient;

	public PublicController(OperationsClient operationsClient, SupplyClient supplyClient) {
		this.operationsClient = operationsClient;
		this.supplyClient = supplyClient;
	}

	@GetMapping("/report")
	String reportForm(Model model) {
		model.addAttribute("zones", supplyClient.zones());
		model.addAttribute("zoneNames", supplyClient.zoneNames());
		model.addAttribute("recent", operationsClient.complaints().stream().limit(10).toList());
		return "report";
	}

	@PostMapping("/report")
	String report(@ModelAttribute OperationsClient.ComplaintForm form, RedirectAttributes redirect) {
		try {
			OperationsClient.Complaint created = operationsClient.createComplaint(form);
			redirect.addFlashAttribute("success", "Thank you. Your ticket number is #" + created.id()
					+ ". Use it with your mobile number on the Track page.");
		}
		catch (ApiException ex) {
			redirect.addFlashAttribute("error", ex.getMessage());
		}
		return "redirect:/report";
	}

	@GetMapping("/track")
	String track(@RequestParam(required = false) Long id, @RequestParam(required = false) String phone, Model model) {
		model.addAttribute("id", id);
		model.addAttribute("phone", phone);
		if (id != null && phone != null) {
			try {
				OperationsClient.Complaint ticket = operationsClient.track(id, phone.trim());
				model.addAttribute("ticket", ticket);
				model.addAttribute("zoneName", supplyClient.zoneNames().get(ticket.zoneId()));
			}
			catch (ApiException ex) {
				model.addAttribute("error", ex.getMessage());
			}
		}
		return "track";
	}

}
