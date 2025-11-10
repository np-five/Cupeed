package com.sparta.cupeed.user.auth.presentation.advice;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthError {

	INTERNAL_SERVER_ERROR("AU000", "서버 내부 오류입니다.", HttpStatus.INTERNAL_SERVER_ERROR),

	AUTH_USER_ID_ALREADY_EXISTS("AU001", "이미 존재하는 아이디입니다.", HttpStatus.BAD_REQUEST),
	AUTH_SLACK_ID_ALREADY_EXISTS("AU002", "이미 존재하는 슬랙 아이디입니다.", HttpStatus.BAD_REQUEST),
	AUTH_USER_NOT_FOUND("AU003", "사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
	AUTH_INVALID_PASSWORD("AU004", "잘못된 비밀번호입니다.", HttpStatus.BAD_REQUEST);

	private final String code;
	private final String message;
	private final HttpStatus status;
}
