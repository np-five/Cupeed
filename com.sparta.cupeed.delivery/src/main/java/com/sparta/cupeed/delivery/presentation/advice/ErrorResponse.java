package com.sparta.cupeed.delivery.presentation.advice;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

//에러 응답 DTO
@Getter
@Setter
class ErrorResponse {
	private int status;
	private String message;
	private String timestamp;

	public ErrorResponse(int status, String message) {
		this.status = status;
		this.message = message;
		this.timestamp = LocalDateTime.now().toString();
	}
}
