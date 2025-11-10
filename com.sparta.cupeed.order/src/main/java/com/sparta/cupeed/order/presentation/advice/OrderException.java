package com.sparta.cupeed.order.presentation.advice;

import lombok.Getter;

@Getter
public class OrderException extends RuntimeException {
	// private final GlobalError error;

	public OrderException(OrderError error) {
		super(error.getErrorMessage());
		// this.error = error;
	}
}
