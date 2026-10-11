package com.ecoflow.web.controller;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.ecoflow.web.client.ApiException;
import com.ecoflow.web.client.ServiceUnavailableException;
import com.ecoflow.web.security.AppUserPrincipal;

@ControllerAdvice
public class GlobalModelAdvice {

	@InitBinder
	void emptyTextToNull(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}

	@ModelAttribute("me")
	AppUserPrincipal currentUser(@AuthenticationPrincipal AppUserPrincipal principal) {
		return principal;
	}

	@ExceptionHandler(ServiceUnavailableException.class)
	@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
	String serviceDown(ServiceUnavailableException ex, Model model) {
		model.addAttribute("title", "A service is not available");
		model.addAttribute("message", ex.getMessage() + ". Check that all microservices are running.");
		return "error";
	}

	@ExceptionHandler(ApiException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	String apiRejected(ApiException ex, Model model) {
		model.addAttribute("title", "Request rejected");
		model.addAttribute("message", ex.getMessage());
		return "error";
	}

}
