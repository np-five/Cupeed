package com.sparta.cupeed.user.presentation.advice;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthError {

	INTERNAL_SERVER_ERROR("AU000", "서버 내부 오류입니다.", HttpStatus.INTERNAL_SERVER_ERROR),
	ACCESS_DENIED("AU001", "접근 권한이 없습니다.", HttpStatus.FORBIDDEN),

	AUTH_USER_ID_ALREADY_EXISTS("AU011", "이미 존재하는 아이디입니다.", HttpStatus.BAD_REQUEST),
	AUTH_SLACK_ID_ALREADY_EXISTS("AU012", "이미 존재하는 슬랙 아이디입니다.", HttpStatus.BAD_REQUEST),
	AUTH_USER_NOT_FOUND("AU013", "사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
	AUTH_INVALID_PASSWORD("AU014", "잘못된 비밀번호입니다.", HttpStatus.BAD_REQUEST),
	AUTH_INVALID_ROLE("AU015", "역할을 찾을 수 없습니다. MASTER/HUB/COMPANY/DELIVERY 중 하나의 역할을 입력해주세요.", HttpStatus.BAD_REQUEST);

	private final String code;
	private final String message;
	private final HttpStatus status;
}
