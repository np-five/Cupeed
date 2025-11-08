package com.sparta.cupeed.slack.application.service;

import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.cupeed.slack.domain.model.Slack;
import com.sparta.cupeed.slack.domain.repository.SlackRepository;
import com.sparta.cupeed.slack.infrastructure.resttemplate.slackapi.dto.SlackSendRequestDtoV1;
import com.sparta.cupeed.slack.infrastructure.resttemplate.slackapi.client.SlackAPIClientV1;
import com.sparta.cupeed.slack.presentation.dto.response.SlackCreateResponseDtoV1;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SlackServiceV1 {

	private final SlackRepository slackRepository;
	private final SlackAPIClientV1 slackApiClientV1;

	@Transactional
	public SlackCreateResponseDtoV1 createSlackMessage(@Valid SlackSendRequestDtoV1 requestDto) {
		SlackSendRequestDtoV1.SlackDto requestSlack = requestDto.getSlack();
		String recipientSlackId = requestSlack.getRecipientSlackId();

		String message = String.format(
			"*주문 완료 알림*\n주문번호: %s\n총 금액: %s원\n수령업체: %s",
			requestSlack.getOrderNumber(),
			requestSlack.getTotalPrice(),
			requestSlack.getRecieveCompanyName()
		);

		Slack.Status status = Slack.Status.REQUESTED;
		String errorMessage = null;

		try {
			slackApiClientV1.sendDirectMessage(recipientSlackId, message);
			// slackApiClientV1.sendMessage(message); // 슬랙메시지 채널 전송
			status = Slack.Status.SUCCESS;
		} catch (Exception e) {
			status = Slack.Status.FAILED;
			errorMessage = e.getMessage();
		}

		// DB 저장
		Slack created = Slack.builder()
			.message(message)
			.sentAt(status == Slack.Status.SUCCESS ? Instant.now() : null)
			.recipientSlackId(requestSlack.getRecipientSlackId())
			.status(status)
			.errorMessage(errorMessage) // 실패 시 에러 메시지 기록
			.createdAt(Instant.now())
			.createdBy("system")
			.build();

		Slack saved = slackRepository.save(created);
		return SlackCreateResponseDtoV1.of(saved);
	}

	// public SlackGetResponseDtoV1 getSlackMessage(@Valid String slackMessageId) {
	// }
	//
	// public SlacksGetResponseDtoV1 getSlackMessages(Pageable pageable) {
	// }
}
