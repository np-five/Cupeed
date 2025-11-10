package com.sparta.cupeed.slack.application.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.cupeed.slack.domain.model.Slack;
import com.sparta.cupeed.slack.domain.repository.SlackRepository;
import com.sparta.cupeed.slack.infrastructure.resttemplate.slackapi.client.SlackAPIClientV1;
import com.sparta.cupeed.slack.presentation.dto.request.SlackDeliveryManagerDMCreateRequestDtoV1;
import com.sparta.cupeed.slack.presentation.dto.request.SlackReceiveCompanyDMCreateRequestDtoV1;
import com.sparta.cupeed.slack.presentation.dto.response.SlackCreateResponseDtoV1;
import com.sparta.cupeed.slack.presentation.dto.response.SlackGetResponseDtoV1;
import com.sparta.cupeed.slack.presentation.dto.response.SlacksGetResponseDtoV1;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SlackServiceV1 {

	private final SlackRepository slackRepository;
	private final SlackAPIClientV1 slackApiClient;

	@Transactional
	public SlackCreateResponseDtoV1 createDMToReciveCompany(SlackReceiveCompanyDMCreateRequestDtoV1 requestDto) {
		String recipientSlackId = requestDto.getRecipientSlackId();

		String message = String.format(
			"*주문 완료 알림*\n주문번호: %s\n총 금액: %s원\n수령업체: %s",
			requestDto.getOrderNumber(),
			requestDto.getTotalPrice(),
			requestDto.getRecieveCompanyName()
		);

		Slack.Status status = Slack.Status.REQUESTED;
		String errorMessage = null;

		try {
			slackApiClient.sendDirectMessage(recipientSlackId, message);
			status = Slack.Status.SUCCESS;
		} catch (Exception e) {
			status = Slack.Status.FAILED;
			errorMessage = e.getMessage();
		}

		Slack created = Slack.builder()
			.message(message)
			.sentAt(status == Slack.Status.SUCCESS ? Instant.now() : null)
			.recipientSlackId(recipientSlackId)
			.status(status)
			.errorMessage(errorMessage) // 실패 시 에러 메시지 기록
			.createdAt(Instant.now())
			.createdBy("system")
			.build();

		Slack saved = slackRepository.save(created);
		return SlackCreateResponseDtoV1.of(saved);
	}

	@Transactional
	public SlackCreateResponseDtoV1 createDMToDliveryManager(SlackDeliveryManagerDMCreateRequestDtoV1 requestDto) {

		Slack.Status status = Slack.Status.REQUESTED;
		String errorMessage = null;

		String recipientSlackId = requestDto.getRecipientSlackId();
		String message = requestDto.getAiResponseText();

		try {
			slackApiClient.sendDirectMessage(recipientSlackId, message);
			status = Slack.Status.SUCCESS;
		} catch (Exception e) {
			status = Slack.Status.FAILED;
			errorMessage = e.getMessage();
		}

		Slack created = Slack.builder()
			.message(requestDto.getAiResponseText())
			.sentAt(status == Slack.Status.SUCCESS ? Instant.now() : null)
			.recipientSlackId(recipientSlackId)
			.status(status)
			.errorMessage(errorMessage) // 실패 시 에러 메시지 기록
			.createdAt(Instant.now())
			.createdBy("system")
			.build();

		Slack saved = slackRepository.save(created);

		return SlackCreateResponseDtoV1.of(saved);
	}

	public SlackGetResponseDtoV1 getSlackMessage(UUID slackMessageId) {
		Slack slack = slackRepository.findById(slackMessageId)
			.orElseThrow(() -> new IllegalArgumentException("슬랙 메시지를 찾을 수 없습니다."));
		return SlackGetResponseDtoV1.of(slack);
	}

	public SlacksGetResponseDtoV1 getSlackMessages(Pageable pageable) {
		// TODO : 검색할 때 쿼리 DSL 적용
		Page<Slack> slacks = slackRepository.findAll(pageable);
		return SlacksGetResponseDtoV1.of(slacks);
	}
}
