package com.sparta.cupeed.gateway.presentation.advice;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GatewayError {

	// 공통
	INTERNAL_SERVER_ERROR("GW000", "서버 내부 오류입니다.", HttpStatus.INTERNAL_SERVER_ERROR),
	GATEWAY_TOKEN_INVALID("GW001", "액세스 토큰이 만료되었거나 유효하지 않습니다.", HttpStatus.UNAUTHORIZED),
	GATEWAY_USER_SERVICE_UNAVAILABLE("GW002", "유저 서비스에 연결할 수 없습니다.", HttpStatus.SERVICE_UNAVAILABLE);

	private final String code;
	private final String message;
	private final HttpStatus status;
}
