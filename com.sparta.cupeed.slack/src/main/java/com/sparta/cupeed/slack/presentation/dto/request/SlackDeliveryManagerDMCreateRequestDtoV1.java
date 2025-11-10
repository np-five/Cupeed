package com.sparta.cupeed.slack.presentation.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SlackDeliveryManagerDMCreateRequestDtoV1 {
	@NotBlank(message = "AI 응답 메시지는 필수입니다.")
	private String aiResponseText;

	@NotBlank(message = "수신자 ID는 필수입니다.")
	private String recipientSlackId;

	@Nullable
	private String errorMessage;
}
