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
import com.sparta.cupeed.slack.presentation.advice.SlackError;
import com.sparta.cupeed.slack.presentation.advice.SlackException;
import com.sparta.cupeed.slack.presentation.dto.request.SlackDeliveryManagerDMCreateRequestDtoV1;
import com.sparta.cupeed.slack.presentation.dto.request.SlackReceiveCompanyDMCreateRequestDtoV1;
import com.sparta.cupeed.slack.presentation.dto.response.SlackCreateResponseDtoV1;
import com.sparta.cupeed.slack.presentation.dto.response.SlackGetResponseDtoV1;
import com.sparta.cupeed.slack.presentation.dto.response.SlacksGetResponseDtoV1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SlackServiceV1 {

	private final SlackRepository slackRepository;
	private final SlackAPIClientV1 slackApiClient;

	@Transactional(noRollbackFor = SlackException.class)
	public SlackCreateResponseDtoV1 createDMToReciveCompany(SlackReceiveCompanyDMCreateRequestDtoV1 requestDto) {
		if (requestDto == null || requestDto.getRecipientSlackId() == null) {
			throw new SlackException(SlackError.SLACK_INVALID_REQUEST);
		}
		String message = String.format(
			":star: *주문 완료 알림* :star:\n주문번호: %s\n총 금액: %s원\n수령업체: %s",
			requestDto.getOrderNumber(),
			requestDto.getTotalPrice(),
			requestDto.getRecieveCompanyName()
		);
		return sendSlackMessage(requestDto.getRecipientSlackId(), message);
	}

	@Transactional(noRollbackFor = SlackException.class)
	public SlackCreateResponseDtoV1 createDMToDliveryManager(SlackDeliveryManagerDMCreateRequestDtoV1 requestDto) {
		if (requestDto == null || requestDto.getRecipientSlackId() == null) {
			throw new SlackException(SlackError.SLACK_INVALID_REQUEST);
		}
		return sendSlackMessage(requestDto.getRecipientSlackId(), requestDto.getAiResponseText());
	}

	/**
	 * 공통 Slack 메시지 전송 및 DB 저장 메서드
	 */
	private SlackCreateResponseDtoV1 sendSlackMessage(String recipientSlackId, String message) {
		Slack.Status status = Slack.Status.REQUESTED;
		String errorMessage = null;

		// Slack API 통신
		try {
			slackApiClient.sendDirectMessage(recipientSlackId, message);
			status = Slack.Status.SUCCESS;
		} catch (Exception e) {
			log.error("[SlackServiceV1] Slack API 통신 실패: {}", e.getMessage());
			status = Slack.Status.FAILED;
			errorMessage = SlackError.SLACK_API_CALL_FAILED.getErrorMessage();
		}

		// DB 저장
		Slack created = Slack.builder()
			.message(message)
			.sentAt(status == Slack.Status.SUCCESS ? Instant.now() : null)
			.recipientSlackId(recipientSlackId)
			.status(status)
			.errorMessage(errorMessage)
			.createdAt(Instant.now())
			.createdBy("system")
			.build();
		Slack saved = slackRepository.save(created);

		if (status == Slack.Status.FAILED) {
			throw new SlackException(SlackError.SLACK_MESSAGE_SEND_FAILED);
		}
		return SlackCreateResponseDtoV1.of(saved);
	}

	@Transactional(readOnly = true)
	public SlackGetResponseDtoV1 getSlackMessage(UUID slackMessageId) {
		Slack slack = slackRepository.findById(slackMessageId)
			.orElseThrow(() -> new SlackException(SlackError.SLACK_MESSAGE_NOT_FOUND));
		return SlackGetResponseDtoV1.of(slack);
	}

	@Transactional(readOnly = true)
	public SlacksGetResponseDtoV1 getSlackMessages(Pageable pageable) {
		// TODO : 검색할 때 쿼리 DSL 적용
		Page<Slack> slacks = slackRepository.findAll(pageable);
		return SlacksGetResponseDtoV1.of(slacks);
	}
}
