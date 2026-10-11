package com.ecoflow.web.controller;

import java.time.LocalDate;

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

@Controller
@RequestMapping("/maintenance")
public class MaintenanceController {

	private final OperationsClient operationsClient;

	public MaintenanceController(OperationsClient operationsClient) {
		this.operationsClient = operationsClient;
	}

	@GetMapping
	String list(Model model) {
		model.addAttribute("tasks", operationsClient.tasks());
		model.addAttribute("assets", operationsClient.assets());
		model.addAttribute("today", LocalDate.now());
		return "maintenance";
	}

	@PostMapping
	String create(@ModelAttribute OperationsClient.TaskForm form, RedirectAttributes redirect) {
		try {
			operationsClient.createTask(form);
			redirect.addFlashAttribute("success", "Task saved");
		}
		catch (ApiException ex) {
			redirect.addFlashAttribute("error", ex.getMessage());
		}
		return "redirect:/maintenance";
	}

	@PostMapping("/{id}/done")
	String markDone(@PathVariable Long id, RedirectAttributes redirect) {
		try {
			operationsClient.markTaskDone(id);
			redirect.addFlashAttribute("success", "Task " + id + " marked as done");
		}
		catch (ApiException ex) {
			redirect.addFlashAttribute("error", ex.getMessage());
		}
		return "redirect:/maintenance";
	}

}
