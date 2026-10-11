package com.ecoflow.web.controller;

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

@Controller
@RequestMapping("/complaints")
public class ComplaintController {

	private final OperationsClient operationsClient;
	private final SupplyClient supplyClient;

	public ComplaintController(OperationsClient operationsClient, SupplyClient supplyClient) {
		this.operationsClient = operationsClient;
		this.supplyClient = supplyClient;
	}

	@GetMapping
	String list(Model model) {
		model.addAttribute("complaints", operationsClient.complaints());
		model.addAttribute("zones", supplyClient.zones());
		model.addAttribute("zoneNames", supplyClient.zoneNames());
		return "complaints";
	}

	@PostMapping
	String create(@ModelAttribute OperationsClient.ComplaintForm form, RedirectAttributes redirect) {
		try {
			OperationsClient.Complaint created = operationsClient.createComplaint(form);
			redirect.addFlashAttribute("success", "Complaint saved as ticket #" + created.id());
		}
		catch (ApiException ex) {
			redirect.addFlashAttribute("error", ex.getMessage());
		}
		return "redirect:/complaints";
	}

	@GetMapping("/{id}")
	String ticket(@PathVariable Long id, Model model) {
		OperationsClient.Complaint complaint = operationsClient.complaint(id);
		Map<Long, String> zoneNames = supplyClient.zoneNames();
		model.addAttribute("c", complaint);
		model.addAttribute("zoneName", zoneNames.get(complaint.zoneId()));
		return "complaint-ticket";
	}

	@PostMapping("/{id}")
	String update(@PathVariable Long id, @ModelAttribute OperationsClient.ComplaintUpdate form, RedirectAttributes redirect) {
		try {
			operationsClient.updateComplaint(id, form);
			redirect.addFlashAttribute("success", "Ticket #" + id + " updated");
		}
		catch (ApiException ex) {
			redirect.addFlashAttribute("error", ex.getMessage());
		}
		return "redirect:/complaints/" + id;
	}

}
