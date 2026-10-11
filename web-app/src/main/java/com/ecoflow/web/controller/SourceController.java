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
import com.ecoflow.web.client.WaterClient;

@Controller
@RequestMapping("/sources")
public class SourceController {

	private final WaterClient waterClient;

	public SourceController(WaterClient waterClient) {
		this.waterClient = waterClient;
	}

	@GetMapping
	String list(Model model) {
		model.addAttribute("sources", waterClient.sources());
		return "sources";
	}

	@PostMapping
	String create(@ModelAttribute WaterClient.SourceForm form, RedirectAttributes redirect) {
		try {
			waterClient.createSource(form);
			redirect.addFlashAttribute("success", "Source '" + form.name() + "' saved");
		}
		catch (ApiException ex) {
			redirect.addFlashAttribute("error", ex.getMessage());
		}
		return "redirect:/sources";
	}

	@GetMapping("/{id}")
	String readings(@PathVariable Long id, Model model) {
		model.addAttribute("source", waterClient.source(id));
		model.addAttribute("readings", waterClient.readings(id));
		model.addAttribute("today", LocalDate.now());
		return "source-readings";
	}

	@PostMapping("/{id}/readings")
	String addReading(@PathVariable Long id, @ModelAttribute WaterClient.ReadingForm form, RedirectAttributes redirect) {
		try {
			waterClient.addReading(id, form);
			redirect.addFlashAttribute("success", "Reading saved");
		}
		catch (ApiException ex) {
			redirect.addFlashAttribute("error", ex.getMessage());
		}
		return "redirect:/sources/" + id;
	}

}
