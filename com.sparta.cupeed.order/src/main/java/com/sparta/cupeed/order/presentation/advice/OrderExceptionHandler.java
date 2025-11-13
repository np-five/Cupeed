package com.sparta.cupeed.order.presentation.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class OrderExceptionHandler {

	@ExceptionHandler(OrderException.class)
	public ResponseEntity<ErrorResponse> handleOrderException(OrderException ex) {
		OrderError error = ex.getError();
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
		public static ErrorResponse from(OrderError error) {
			return new ErrorResponse(
				Instant.now(),
				error.getHttpStatus().value(),
				error.name(),
				error.getErrorMessage()
			);
		}
	}
}
