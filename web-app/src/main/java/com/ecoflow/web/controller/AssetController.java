package com.ecoflow.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ecoflow.web.client.ApiException;
import com.ecoflow.web.client.OperationsClient;
import com.ecoflow.web.client.SupplyClient;

@Controller
@RequestMapping("/assets")
public class AssetController {

	private final OperationsClient operationsClient;
	private final SupplyClient supplyClient;

	public AssetController(OperationsClient operationsClient, SupplyClient supplyClient) {
		this.operationsClient = operationsClient;
		this.supplyClient = supplyClient;
	}

	@GetMapping
	String list(Model model) {
		model.addAttribute("assets", operationsClient.assets());
		model.addAttribute("zones", supplyClient.zones());
		model.addAttribute("zoneNames", supplyClient.zoneNames());
		return "assets";
	}

	@PostMapping
	String create(@ModelAttribute OperationsClient.AssetForm form, RedirectAttributes redirect) {
		try {
			operationsClient.createAsset(form);
			redirect.addFlashAttribute("success", "Asset '" + form.name() + "' saved");
		}
		catch (ApiException ex) {
			redirect.addFlashAttribute("error", ex.getMessage());
		}
		return "redirect:/assets";
	}

}
