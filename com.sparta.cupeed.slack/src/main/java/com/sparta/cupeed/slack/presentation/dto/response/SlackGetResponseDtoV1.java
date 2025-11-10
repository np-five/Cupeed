package com.sparta.cupeed.slack.presentation.dto.response;

import java.time.Instant;
import java.util.UUID;

import com.sparta.cupeed.slack.domain.model.Slack;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SlackGetResponseDtoV1 {
	private final SlackDto slack;

	public static SlackGetResponseDtoV1 of(Slack slack) {
		return SlackGetResponseDtoV1.builder()
			.slack(SlackDto.from(slack)).build();
	}

	@Getter
	@Builder
	public static class SlackDto {

		private final UUID id;
		private final String recipientSlackId;
		private final String message;
		private final Instant sentAt;
		private final Slack.Status status;
		private final String errorMessage;

		public static SlackDto from(Slack slack) {
			return SlackDto.builder()
				.id(slack.getId())
				.recipientSlackId(slack.getRecipientSlackId())
				.message(slack.getMessage())
				.status(slack.getStatus())
				.errorMessage(slack.getErrorMessage())
				.sentAt(slack.getSentAt())
				.build();
		}
	}
}
