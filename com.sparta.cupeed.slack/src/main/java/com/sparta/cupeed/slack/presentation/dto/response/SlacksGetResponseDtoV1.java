package com.sparta.cupeed.slack.presentation.dto.response;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedModel;

import com.sparta.cupeed.slack.domain.model.Slack;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
public class SlacksGetResponseDtoV1 {
	private final SlackPageDto slackPage;

	public static SlacksGetResponseDtoV1 of(Page<Slack> slackPage) {
		return SlacksGetResponseDtoV1.builder()
			.slackPage(new SlackPageDto(slackPage))
			.build();
	}

	@Getter
	@ToString
	public static class SlackPageDto extends PagedModel<SlackPageDto.SlackDto> {
		public SlackPageDto(Page<Slack> slackPage) {
			super(
				new PageImpl<>(
					SlackDto.from(slackPage.getContent()),
					slackPage.getPageable(),
					slackPage.getTotalElements()
				)
			);
		}

		public SlackPageDto(SlackDto... slackDtoArray) {
			super(new PageImpl<>(List.of(slackDtoArray)));
		}

		@Getter
		@Builder
		public static class SlackDto {

			private final String id;
			private final String recipientSlackId; // UUID로 변경해야 함
			private final Slack.Status status;
			private final Instant sentAt;

			private static List<SlackDto> from(List<Slack> slackList) {
				return slackList.stream()
					.map(SlackDto::from)
					.toList();
			}

			public static SlackDto from(Slack slack) {
				return SlackDto.builder()
					.id(String.valueOf(slack.getId()))
					.recipientSlackId(slack.getRecipientSlackId())
					.status(slack.getStatus())
					.sentAt(slack.getSentAt())
					.build();
			}
		}
	}
}
