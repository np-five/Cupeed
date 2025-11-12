package com.sparta.cupeed.user.presentation.advice;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthError {

	// 공통
	INTERNAL_SERVER_ERROR("AU000", "서버 내부 오류입니다.", HttpStatus.INTERNAL_SERVER_ERROR),
	ACCESS_DENIED("AU001", "접근 권한이 없습니다.", HttpStatus.FORBIDDEN),
	AUTH_USER_NOT_FOUND("AU002", "사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
	INVALID_INPUT("AU003", "입력값이 잘못되었습니다.", HttpStatus.BAD_REQUEST),

	// 회원가입
	AUTH_USER_ID_ALREADY_EXISTS("AU010", "이미 존재하는 아이디입니다.", HttpStatus.BAD_REQUEST),
	AUTH_SLACK_ID_ALREADY_EXISTS("AU011", "이미 존재하는 슬랙 아이디입니다.", HttpStatus.BAD_REQUEST),
	AUTH_INVALID_ROLE("AU012", "역할을 찾을 수 없습니다. MASTER/HUB/COMPANY/DELIVERY 중 하나의 역할을 입력해주세요.", HttpStatus.BAD_REQUEST),
	AUTH_EMPTY_HUB_NAME("AU013", "허브 이름을 입력해주세요.", HttpStatus.BAD_REQUEST),
	AUTH_EMPTY_COMPANY_INFO("AU014", "업체 정보를 입력해주세요.", HttpStatus.BAD_REQUEST),
	AUTH_INVALID_DELIVERY_TYPE("AU012", "배달 타입을 찾을 수 없습니다. HUB/COMPANY 중 하나의 역할을 입력해주세요.", HttpStatus.BAD_REQUEST),
	AUTH_INVALID_DELIVERY_ORDER("AU012", "올바른 배달 순번을 입력해주세요.", HttpStatus.BAD_REQUEST),

	// 로그인
	AUTH_INVALID_PASSWORD("AU020", "잘못된 비밀번호입니다.", HttpStatus.BAD_REQUEST),

	// 사용자 상태 변경
	AUTH_STATUS_NOT_PENDING("AU030", "PENDING 상태의 사용자의 상태만 변경할 수 있습니다.", HttpStatus.FORBIDDEN),
	AUTH_INVALID_STATUS("AU031", "상태값을 찾을 수 없습니다. APPROVED/REJECTED 중 하나의 상태를 입력해주세요.", HttpStatus.BAD_REQUEST),
	AUTH_UPDATE_MASTER_FORBIDDEN("AU032", "MASTER 사용자의 상태값을 변경할 수 없습니다.", HttpStatus.FORBIDDEN);

	private final String code;
	private final String message;
	private final HttpStatus status;
}
