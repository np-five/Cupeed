package com.sparta.cupeed.ai.presentation.advice;

import java.time.Instant;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AiExceptionHandler {
	@ExceptionHandler(AiException.class)
	public ResponseEntity<ErrorResponse> handleOrderException(AiException ex) {
		AiError error = ex.getError();
		return ResponseEntity.status(error.getHttpStatus())
			.body(ErrorResponse.from(error));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
		return ResponseEntity.internalServerError()
			.body(new ErrorResponse(
				Instant.now(),
				500,
				"INTERNAL_SERVER_ERROR",
				ex.getMessage()
			));
	}

	public record ErrorResponse(
		Instant timestamp,
		int status,
		String error,
		String message
	) {
		public static ErrorResponse from(AiError error) {
			return new ErrorResponse(
				Instant.now(),
				error.getHttpStatus().value(),
				error.name(),
				error.getErrorMessage()
			);
		}
	}
}
