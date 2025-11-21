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
public class UserExceptionHandler {

	@ExceptionHandler(UserException.class)
	public ResponseEntity<ApiResponse<Void>> handleAuthException(UserException ex) {
		UserError userError = ex.getUserError();
		log.error("AuthException occurred: code={}, message={}", userError.getCode(), userError.getMessage());

		return ResponseEntity.status(userError.getStatus()).body(ApiResponse.error(userError));
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ApiResponse<Void>> handleAccessDeniedException(AccessDeniedException ex) {
		UserError userError = UserError.ACCESS_DENIED;
		log.error(
			"AuthException occurred: code={}, message={}",
			userError.getCode(),
			userError.getMessage() + '\n' + ex.getMessage());

		return ResponseEntity.status(userError.getStatus()).body(ApiResponse.error(userError));
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
		UserError userError = UserError.INVALID_INPUT;

		log.error("[{}] {}", userError.getCode(), userError.getMessage(), exception);

		return ResponseEntity.status(userError.getStatus()).body(ApiResponse.error(userError));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<Void>> handleGeneralException(Exception ex) {
		UserError userError = UserError.INTERNAL_SERVER_ERROR;
		log.error("Unexpected exception occurred: {}", ex.getMessage(), ex);

		return ResponseEntity.status(userError.getStatus()).body(ApiResponse.error(userError));
	}
}
