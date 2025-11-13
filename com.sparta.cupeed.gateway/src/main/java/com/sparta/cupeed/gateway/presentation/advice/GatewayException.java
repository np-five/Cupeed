package com.sparta.cupeed.gateway.presentation.advice;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GatewayException extends RuntimeException {

	private final GatewayError gatewayError;
}
