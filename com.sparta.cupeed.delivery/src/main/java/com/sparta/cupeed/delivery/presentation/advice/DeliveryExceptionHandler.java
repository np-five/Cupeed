package com.sparta.cupeed.delivery.presentation.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//전체 예외 처리
@RestControllerAdvice
public class DeliveryExceptionHandler {

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
		ErrorResponse errorResponse = new ErrorResponse(
			HttpStatus.BAD_REQUEST.value(),
			ex.getMessage() != null ? ex.getMessage() : "잘못된 요청입니다."
		);
		return ResponseEntity.badRequest().body(errorResponse);
	}

	@ExceptionHandler(IllegalStateException.class)
	public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException ex) {
		ErrorResponse errorResponse = new ErrorResponse(
			HttpStatus.CONFLICT.value(),
			ex.getMessage() != null ? ex.getMessage() : "처리할 수 없는 상태입니다."
		);
		return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception ex) {
		ErrorResponse errorResponse = new ErrorResponse(
			HttpStatus.INTERNAL_SERVER_ERROR.value(),
			"서버 오류가 발생했습니다."
		);
		return ResponseEntity.internalServerError().body(errorResponse);
	}
}
