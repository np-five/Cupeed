package com.sparta.cupeed.delivery.presentation.advice;

import java.time.LocalDateTime;

//에러 응답 DTO
class ErrorResponse {
	private int status;
	private String message;
	private String timestamp;

	public ErrorResponse(int status, String message) {
		this.status = status;
		this.message = message;
		this.timestamp = LocalDateTime.now().toString();
	}

	// Getter/Setter
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
}
