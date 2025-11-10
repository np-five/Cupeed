package com.sparta.cupeed.ai.presentation.advice;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AIError {
	AI_REQUEST_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "AI 요청 중 오류가 발생했습니다."),
	AI_RESPONSE_INVALID(HttpStatus.BAD_GATEWAY, "AI로부터 유효한 응답을 받지 못했습니다."),
	AI_HISTORY_NOT_FOUND(HttpStatus.NOT_FOUND, "AI 요청 내역을 찾을 수 없습니다."),
	AI_PROMPT_BUILD_FAILED(HttpStatus.BAD_REQUEST, "AI 프롬프트 생성 중 오류가 발생했습니다."),
	AI_SLACK_NOTIFICATION_FAILED(HttpStatus.SERVICE_UNAVAILABLE, "AI 응답을 슬랙으로 전송하는 데 실패했습니다."),
	AI_SAVE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "AI 요청 결과 저장 중 오류가 발생했습니다."),

	// GeminiAPIClient Error
	GEMINI_AI_KEY_MISSING(HttpStatus.BAD_REQUEST, "AI API Key가 비어 있습니다."),
	GEMINI_AI_API_CALL_FAILED(HttpStatus.BAD_GATEWAY, "Google Gemini API 호출 실패"),
	GEMINI_AI_JSON_PARSE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "AI 응답 파싱 중 오류 발생");

	private final HttpStatus httpStatus;
	private final String errorMessage;
}
