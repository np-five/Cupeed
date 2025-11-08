package com.sparta.cupeed.slack.presentation.dto.response;

import com.sparta.cupeed.slack.domain.model.Slack;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SlackCreateResponseDtoV1 {
	private final SlackDto slack;

	public static SlackCreateResponseDtoV1 of(Slack slack) {
		return SlackCreateResponseDtoV1.builder()
			.slack(SlackDto.from(slack))
			.build();
	}

	@Getter
	@Builder
	public static class SlackDto {
		private final String id;

		public static SlackDto from(Slack slack) {
			return SlackDto.builder()
				.id(String.valueOf(slack.getId()))
				.build();
		}
	}


}
