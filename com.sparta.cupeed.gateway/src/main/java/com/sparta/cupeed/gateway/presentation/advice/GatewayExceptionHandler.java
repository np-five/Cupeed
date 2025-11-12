package com.sparta.cupeed.gateway.presentation.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GatewayExceptionHandler {

	@ExceptionHandler(GatewayException.class)
	public ResponseEntity<ApiResponse<Void>> handleAuthException(GatewayException ex) {
		GatewayError gatewayError = ex.getGatewayError();
		log.error("AuthException occurred: code={}, message={}", gatewayError.getCode(), gatewayError.getMessage());

		return ResponseEntity.status(gatewayError.getStatus()).body(ApiResponse.error(gatewayError));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<Void>> handleGeneralException(Exception ex) {
		GatewayError gatewayError = GatewayError.INTERNAL_SERVER_ERROR;
		log.error("Unexpected exception occurred: {}", ex.getMessage(), ex);

		return ResponseEntity.status(gatewayError.getStatus()).body(ApiResponse.error(gatewayError));
	}
}
