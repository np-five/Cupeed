package com.sparta.cupeed.ai.infrastructure.slack.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SlackMessageCreateRequestDtoV1 {
	@NotNull(message = "수신자 ID는 필수입니다.")
	private String recipientSlackId;

	@NotBlank(message = "AI 응답 메시지는 필수입니다.")
	private String aiResponseText;

	@Nullable
	private String errorMessage;
}
