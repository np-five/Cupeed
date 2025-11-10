package com.sparta.cupeed.ai.presentation.advice;

import lombok.Getter;

@Getter
public class AIException extends RuntimeException {
	private final AIError error;

	public AIException(AIError error) {
		super(error.getErrorMessage());
		this.error = error;
	}

	public AIException(AIError error, Throwable cause) {
		super(error.getErrorMessage(), cause);
		this.error = error;
	}
}
