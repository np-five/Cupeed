package com.sparta.cupeed.user.presentation.advice;

import org.springframework.validation.BindingResult;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) // null인 필드는 Json응답에서 제외, 성공한 요청에는 errors가 필요 없으니 JSON에서 아예 빠지도록 설정
public class AuthErrorResponse<T> {

	private String status; // "SUCCESS" 또는 "FAIL"
	private String code;   // S200, U001 등
	private String message;
	private T data;        // 성공 시 응답 데이터

	public static <T> AuthErrorResponse<T> success(String message, T data) {
		return AuthErrorResponse.<T>builder()
			.status("SUCCESS")
			.code("200")
			.message(message)
			.data(data)
			.build();
	}

	public static AuthErrorResponse<Void> success(String message) {
		return success(message, null);
	}

	public static AuthErrorResponse<Void> error(AuthError errorCode) {
		return AuthErrorResponse.<Void>builder()
			.status("FAIL")
			.code(errorCode.getCode())
			.message(errorCode.getMessage())
			.build();
	}

	public static AuthErrorResponse<Void> error(AuthError errorCode, BindingResult bindingResult) {
		return AuthErrorResponse.<Void>builder()
			.status("FAIL")
			.code(errorCode.getCode())
			.message(errorCode.getMessage())
			.build();
	}

	public static AuthErrorResponse<Void> error(AuthError errorCode, String message) {
		return AuthErrorResponse.<Void>builder()
			.status("FAIL")
			.code(errorCode.getCode())
			.message(message)
			.build();
	}

	public static AuthErrorResponse<Void> error(String errorCode, String message) {
		return AuthErrorResponse.<Void>builder()
			.status("FAIL")
			.code(errorCode)
			.message(message)
			.build();
	}
}
