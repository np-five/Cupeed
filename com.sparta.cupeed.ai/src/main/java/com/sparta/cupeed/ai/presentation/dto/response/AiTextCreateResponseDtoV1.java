package com.sparta.cupeed.ai.presentation.dto.response;

import com.sparta.cupeed.ai.domain.model.Ai;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AiTextCreateResponseDtoV1 {
	private final AiDto ai;

	public static AiTextCreateResponseDtoV1 of(Ai ai) {
		return AiTextCreateResponseDtoV1.builder()
			.ai(AiDto.from(ai))
			.build();
	}

	@Getter
	@Builder
	public static class AiDto {
		private final String id;

		public static AiDto from(Ai ai) {
			return AiDto.builder()
				.id(String.valueOf(ai.getId()))
				.build();
		}
	}
}
