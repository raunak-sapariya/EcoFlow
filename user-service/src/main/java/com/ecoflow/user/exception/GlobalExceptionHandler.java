package com.ecoflow.user.exception;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

/**
 * Turns errors into one JSON shape, e.g. {"status":404,"detail":"User 7 not found"}.
 * The web-app shows the "detail" text to the user.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	/** Thrown by our services: throw new ResponseStatusException(HttpStatus.NOT_FOUND, "...") */
	@ExceptionHandler(ResponseStatusException.class)
	ProblemDetail statusError(ResponseStatusException ex) {
		return ProblemDetail.forStatusAndDetail(ex.getStatusCode(), ex.getReason());
	}

	/** @Valid failed on a request body: list every broken field in one sentence. */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	ProblemDetail validation(MethodArgumentNotValidException ex) {
		String detail = ex.getBindingResult().getFieldErrors().stream()
				.map(error -> error.getField() + " " + error.getDefaultMessage())
				.collect(Collectors.joining("; "));
		return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, detail);
	}

}
