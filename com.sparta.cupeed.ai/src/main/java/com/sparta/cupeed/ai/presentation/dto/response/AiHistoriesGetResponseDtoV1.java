package com.sparta.cupeed.ai.presentation.dto.response;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedModel;

import com.sparta.cupeed.ai.domain.model.Ai;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
public class AiHistoriesGetResponseDtoV1 {
	private final AiHistoryPageDto aiHistoryPage;

	public static AiHistoriesGetResponseDtoV1 of(Page<Ai> aiHistoryPage) {
		return AiHistoriesGetResponseDtoV1.builder()
			.aiHistoryPage(new AiHistoryPageDto(aiHistoryPage)).build();
	}

	@Getter
	@ToString
	public static class AiHistoryPageDto extends PagedModel<AiHistoryPageDto.AiHistoryDto> {
		public AiHistoryPageDto(Page<Ai> aiHistoryPage) {
			super(
				new PageImpl<>(
					AiHistoryDto.from(aiHistoryPage.getContent()),
					aiHistoryPage.getPageable(),
					aiHistoryPage.getTotalElements()
				)
			);
		}

		public AiHistoryPageDto(AiHistoryDto... aiHistoryDtoArray) {
			super(new PageImpl<>(List.of(aiHistoryDtoArray)));
		}

		@Getter
		@Builder
		public static class AiHistoryDto {
			private final UUID id;
			private final UUID orderId;
			private final Ai.Status status;
			private final Instant createdAt;

			private static List<AiHistoryDto> from(List<Ai> aiHistoryList) {
				return aiHistoryList.stream().map(AiHistoryDto::from).toList();
			}

			public static AiHistoryDto from(Ai aiHistory) {
				return AiHistoryDto.builder()
					.id(aiHistory.getId())
					.orderId(aiHistory.getOrderId())
					.status(aiHistory.getStatus())
					.createdAt(aiHistory.getCreatedAt())
					.build();
			}
		}

	}
}
