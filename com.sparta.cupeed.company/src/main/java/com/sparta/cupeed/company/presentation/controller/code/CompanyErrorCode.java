package com.sparta.cupeed.company.presentation.controller.code;

import org.springframework.http.HttpStatus;

import com.sparta.cupeed.global.code.ResponseCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CompanyErrorCode implements ResponseCode {

	COMPANY_NOT_FOUND(HttpStatus.NOT_FOUND, "COMP-001", "업체를 찾을 수 없습니다."),
	BUSINESS_NUMBER_EXISTS(HttpStatus.BAD_REQUEST, "COMP-002", "이미 존재하는 사업자 등록번호입니다."),
	HUB_NOT_FOUND(HttpStatus.NOT_FOUND, "COMP-003", "해당 허브를 찾을 수 없습니다."),
	INVALID_MANAGER(HttpStatus.BAD_REQUEST, "COMP-004", "유효하지 않은 매니저 ID입니다."),
	BUSINESS_NUMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "COMP-005", "사업자번호로 회사를 찾을 수 없습니다."),
	NOT_AUTHORIZED(HttpStatus.FORBIDDEN, "COMP-006", "해당 업체를 수정할 권한이 없습니다.");

	private final HttpStatus status;
	private final String code;
	private final String message;

}
