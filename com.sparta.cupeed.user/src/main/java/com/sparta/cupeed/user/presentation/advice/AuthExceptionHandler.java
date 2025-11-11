package com.sparta.cupeed.user.presentation.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class AuthExceptionHandler {

	@ExceptionHandler(AuthException.class)
	public ResponseEntity<AuthErrorResponse<Void>> handleAuthException(AuthException ex) {
		AuthError authError = ex.getAuthError();
		log.error("AuthException occurred: code={}, message={}", authError.getCode(), authError.getMessage());

		return ResponseEntity.status(authError.getStatus()).body(AuthErrorResponse.error(authError));
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<AuthErrorResponse<Void>> handleAccessDeniedException(AccessDeniedException ex) {
		AuthError authError = AuthError.ACCESS_DENIED;
		log.error(
			"AuthException occurred: code={}, message={}",
			authError.getCode(),
			authError.getMessage() + '\n' + ex.getMessage());

		return ResponseEntity.status(authError.getStatus()).body(AuthErrorResponse.error(authError));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<AuthErrorResponse<Void>> handleGeneralException(Exception ex) {
		AuthError authError = AuthError.INTERNAL_SERVER_ERROR;
		log.error("Unexpected exception occurred: {}", ex.getMessage(), ex);

		return ResponseEntity.status(authError.getStatus()).body(AuthErrorResponse.error(authError));
	}
}
