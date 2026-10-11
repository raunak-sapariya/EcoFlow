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
import com.ecoflow.web.client.WaterClient;

@Controller
@RequestMapping("/quality")
public class QualityController {

	private final WaterClient waterClient;

	public QualityController(WaterClient waterClient) {
		this.waterClient = waterClient;
	}

	@GetMapping
	String list(Model model) {
		model.addAttribute("tests", waterClient.qualityTests());
		model.addAttribute("sources", waterClient.sources());
		model.addAttribute("today", LocalDate.now());
		return "quality";
	}

	@PostMapping
	String record(@ModelAttribute WaterClient.QualityForm form, RedirectAttributes redirect) {
		try {
			WaterClient.QualityTest test = waterClient.recordTest(form);
			redirect.addFlashAttribute("success", "Test saved. Result: " + test.result());
		}
		catch (ApiException ex) {
			redirect.addFlashAttribute("error", ex.getMessage());
		}
		return "redirect:/quality";
	}

}
