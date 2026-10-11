package com.ecoflow.web.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ecoflow.web.client.ApiException;
import com.ecoflow.web.client.UserClient;
import com.ecoflow.web.security.AppUserPrincipal;

@Controller
@RequestMapping("/users")
public class UserAdminController {

	private final UserClient userClient;

	public UserAdminController(UserClient userClient) {
		this.userClient = userClient;
	}

	@GetMapping
	String list(Model model) {
		model.addAttribute("users", userClient.findAll());
		return "users";
	}

	@PostMapping
	String create(@ModelAttribute UserClient.NewUser form, RedirectAttributes redirect) {
		try {
			userClient.create(form);
			redirect.addFlashAttribute("success", "Account '" + form.username() + "' saved");
		}
		catch (ApiException ex) {
			redirect.addFlashAttribute("error", ex.getMessage());
		}
		return "redirect:/users";
	}

	@PostMapping("/{id}/delete")
	String delete(@PathVariable Long id, @AuthenticationPrincipal AppUserPrincipal me, RedirectAttributes redirect) {
		if (id.equals(me.getId())) {
			redirect.addFlashAttribute("error", "You cannot delete your own account");
			return "redirect:/users";
		}
		try {
			userClient.delete(id);
			redirect.addFlashAttribute("success", "Account deleted");
		}
		catch (ApiException ex) {
			redirect.addFlashAttribute("error", ex.getMessage());
		}
		return "redirect:/users";
	}

}
