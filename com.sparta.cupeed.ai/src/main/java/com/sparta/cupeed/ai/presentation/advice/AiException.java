package com.sparta.cupeed.ai.presentation.advice;

import lombok.Getter;

@Getter
public class AiException extends RuntimeException {
	private final AiError error;

	public AiException(AiError error) {
		super(error.getErrorMessage());
		this.error = error;
	}

	public AiException(AiError error, Throwable cause) {
		super(error.getErrorMessage(), cause);
		this.error = error;
	}
}
