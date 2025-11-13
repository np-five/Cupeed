package com.sparta.cupeed.slack.presentation.advice;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SlackError {
	SLACK_MESSAGE_SEND_FAILED(HttpStatus.BAD_GATEWAY, "슬랙 메시지 전송에 실패했습니다."),
	SLACK_MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "슬랙 메시지를 찾을 수 없습니다."),
	SLACK_INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
	SLACK_INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "슬랙 서비스 내부 오류가 발생했습니다."),
	SLACK_API_CALL_FAILED(HttpStatus.BAD_GATEWAY, "Slack API 호출 실패"),
	SLACK_ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다.");

	private final HttpStatus httpStatus;
	private final String errorMessage;
}
