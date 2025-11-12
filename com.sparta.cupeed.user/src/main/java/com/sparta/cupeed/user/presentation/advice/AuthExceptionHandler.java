package com.sparta.cupeed.user.presentation.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class AuthExceptionHandler {

	@ExceptionHandler(AuthException.class)
	public ResponseEntity<ApiResponse<Void>> handleAuthException(AuthException ex) {
		AuthError authError = ex.getAuthError();
		log.error("AuthException occurred: code={}, message={}", authError.getCode(), authError.getMessage());

		return ResponseEntity.status(authError.getStatus()).body(ApiResponse.error(authError));
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ApiResponse<Void>> handleAccessDeniedException(AccessDeniedException ex) {
		AuthError authError = AuthError.ACCESS_DENIED;
		log.error(
			"AuthException occurred: code={}, message={}",
			authError.getCode(),
			authError.getMessage() + '\n' + ex.getMessage());

		return ResponseEntity.status(authError.getStatus()).body(ApiResponse.error(authError));
	}

	// 입력값 검증
	@ExceptionHandler({
		MethodArgumentNotValidException.class,
		MethodArgumentTypeMismatchException.class,
		MissingPathVariableException.class,
		MissingServletRequestParameterException.class,
		ConstraintViolationException.class,
		BindException.class
	})
	public ResponseEntity<ApiResponse<Void>> handleValidation(Exception exception) {
		AuthError authError = AuthError.INVALID_INPUT;

		log.error("[{}] {}", authError.getCode(), authError.getMessage(), exception);

		return ResponseEntity.status(authError.getStatus()).body(ApiResponse.error(authError));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<Void>> handleGeneralException(Exception ex) {
		AuthError authError = AuthError.INTERNAL_SERVER_ERROR;
		log.error("Unexpected exception occurred: {}", ex.getMessage(), ex);

		return ResponseEntity.status(authError.getStatus()).body(ApiResponse.error(authError));
	}
}
