package com.sparta.cupeed.slack.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "발송 담당자에게 전송할 배송요약메시지 DM 생성 요청 DTO")
public class SlackDeliveryManagerDMCreateRequestDtoV1 {

	@NotBlank(message = "AI 응답 메시지는 필수입니다.")
	@Schema(description = "Gemini가 생성한 AI 응답 메시지")
	private String aiResponseText;

	@NotBlank(message = "수신자 ID는 필수입니다.")
	@Schema(description = "슬랙 메시지 수신자 ID",
		example = "U12345678")
	private String recipientSlackId;

	@Nullable
	@Schema(description = "에러 메시지(있을 경우)", nullable = true)
	private String errorMessage;
}
