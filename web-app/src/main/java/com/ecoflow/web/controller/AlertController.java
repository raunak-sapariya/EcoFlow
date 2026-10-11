package com.ecoflow.web.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ecoflow.web.client.ApiException;
import com.ecoflow.web.client.OperationsClient;
import com.ecoflow.web.security.AppUserPrincipal;

@Controller
@RequestMapping("/alerts")
public class AlertController {

	private final OperationsClient operationsClient;

	public AlertController(OperationsClient operationsClient) {
		this.operationsClient = operationsClient;
	}

	@GetMapping
	String list(Model model) {
		model.addAttribute("alerts", operationsClient.alerts());
		return "alerts";
	}

	@PostMapping
	String create(@ModelAttribute OperationsClient.AlertForm form, @AuthenticationPrincipal AppUserPrincipal me,
			RedirectAttributes redirect) {
		try {
			operationsClient.createAlert(new OperationsClient.AlertForm(form.title(), form.message(), form.severity(),
					me.getFullName()));
			redirect.addFlashAttribute("success", "Alert saved");
		}
		catch (ApiException ex) {
			redirect.addFlashAttribute("error", ex.getMessage());
		}
		return "redirect:/alerts";
	}

}
