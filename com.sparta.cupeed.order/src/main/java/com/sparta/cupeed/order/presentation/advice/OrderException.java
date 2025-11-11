package com.sparta.cupeed.order.presentation.advice;

import lombok.Getter;

@Getter
public class OrderException extends RuntimeException {
	private final OrderError error;

	public OrderException(OrderError error) {
		super(error.getErrorMessage());
		this.error = error;
	}
}
