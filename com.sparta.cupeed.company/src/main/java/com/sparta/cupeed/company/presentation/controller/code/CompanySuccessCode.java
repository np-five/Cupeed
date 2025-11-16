package com.sparta.cupeed.company.presentation.controller.code;

import org.springframework.http.HttpStatus;

import com.sparta.cupeed.global.code.ResponseCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CompanySuccessCode implements ResponseCode {

	OK(HttpStatus.OK, "COMP-200", "요청이 성공적으로 처리되었습니다."),
	CREATED(HttpStatus.CREATED, "COMP-201", "업체가 성공적으로 생성되었습니다."),
	NO_CONTENT(HttpStatus.NO_CONTENT, "COMP-204", "요청은 성공했지만 응답 본문이 없습니다.");


	private final HttpStatus status;
	private final String code;
	private final String message;

}
