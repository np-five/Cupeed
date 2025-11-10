package com.sparta.cupeed.ai.presentation.dto.response;

import java.time.Instant;
import java.util.UUID;

import com.sparta.cupeed.ai.domain.model.Ai;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AiHistoryGetResponseDtoV1 {
	private final AiHistoryDto aiHistory;

	public static AiHistoryGetResponseDtoV1 of(Ai aiHistory) {
		return AiHistoryGetResponseDtoV1.builder()
			.aiHistory(AiHistoryDto.from(aiHistory)).build();
	}

	@Getter
	@Builder
	public static class AiHistoryDto {
		private final UUID id;
		private final UUID orderId;
		private final String aiResponseText;
		private final Ai.Status status;
		private final String errorMessage;
		private final Instant createdAt;
		private final String createdBy;

		public static AiHistoryDto from(Ai aiHistory) {
			return AiHistoryDto.builder()
				.id(aiHistory.getId())
				.orderId(aiHistory.getOrderId())
				.aiResponseText(aiHistory.getAiResponseText())
				.status(aiHistory.getStatus())
				.errorMessage(aiHistory.getErrorMessage())
				.createdAt(aiHistory.getCreatedAt())
				.createdBy(aiHistory.getCreatedBy())
				.build();
		}
	}
}
