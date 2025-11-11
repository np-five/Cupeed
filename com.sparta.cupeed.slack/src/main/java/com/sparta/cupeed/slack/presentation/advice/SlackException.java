package com.sparta.cupeed.slack.presentation.advice;

import lombok.Getter;

@Getter
public class SlackException extends RuntimeException {
	private final SlackError error;

	public SlackException(SlackError error) {
		super(error.getErrorMessage());
		this.error = error;
	}
}
