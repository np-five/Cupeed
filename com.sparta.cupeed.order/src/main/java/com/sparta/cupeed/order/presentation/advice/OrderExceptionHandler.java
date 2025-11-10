package com.sparta.cupeed.order.presentation.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class OrderExceptionHandler {

	@ExceptionHandler(OrderException.class)
	public ResponseEntity<Map<String, Object>> handleOrderException(OrderException ex) {
		// 메시지에서 OrderError 찾기 (예: Enum 이름이 포함되어 있지 않으면 메시지만 사용)
		OrderError matchedError = null;
		for (OrderError e : OrderError.values()) {
			if (e.getErrorMessage().equals(ex.getMessage())) {
				matchedError = e;
				break;
			}
		}

		if (matchedError == null) {
			// 혹시 Enum을 못찾는 경우 fallback
			return ResponseEntity.internalServerError().body(Map.of(
				"timestamp", Instant.now(),
				"status", 500,
				"error", "Internal Server Error",
				"message", ex.getMessage()
			));
		}

		return ResponseEntity.status(matchedError.getHttpStatus())
			.body(Map.of(
				"timestamp", Instant.now(),
				"status", matchedError.getHttpStatus().value(),
				"error", matchedError.name(),
				"message", matchedError.getErrorMessage()
			));
	}
}
