package com.sparta.cupeed.ai.application.service;

import java.util.UUID;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.cupeed.ai.domain.model.Ai;
import com.sparta.cupeed.ai.domain.repository.AiRepository;
import com.sparta.cupeed.ai.infrastructure.resttemplate.geminiapi.client.GeminiAPIClientV1;
import com.sparta.cupeed.ai.infrastructure.slack.client.SlackClientV1;
import com.sparta.cupeed.ai.infrastructure.slack.dto.SlackMessageCreateRequestDtoV1;
import com.sparta.cupeed.ai.presentation.advice.AiError;
import com.sparta.cupeed.ai.presentation.advice.AiException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiAsyncServiceV1 {

	private final GeminiAPIClientV1 geminiAPIClient;
	private final AiRepository aiRepository;
	private final SlackClientV1 slackClient;

	@Async("aiTaskExecutor")
	public void processGeminiApiAsync(UUID aiId, String prompt, String slackId) {
		String aiResponseText = null;
		String errorMessage = null;
		Ai.Status status;
		try {
			aiResponseText = geminiAPIClient.createAiText(prompt);

			if (aiResponseText == null || aiResponseText.isBlank()) {
				status = Ai.Status.FAILED;
				errorMessage = AiError.GEMINI_RESPONSE_INVALID.getErrorMessage();
			} else {
				status = Ai.Status.SUCCESS;
			}
		} catch (Exception e) {
			status = Ai.Status.FAILED;
			errorMessage = AiError.GEMINI_AI_API_CALL_FAILED.getErrorMessage();
			log.error("[AiServiceV1] Gemini API 실패: {}", e.getMessage());
		}
		updateAiResult(aiId, aiResponseText, status, errorMessage);
		sendSlackNotification(slackId, aiResponseText, errorMessage);
	}

	@Transactional
	public void updateAiResult(UUID aiId, String aiResponseText, Ai.Status status, String errorMessage) {
		Ai ai = aiRepository.findById(aiId)
			.orElseThrow(() -> new AiException(AiError.AI_HISTORY_NOT_FOUND));
		Ai updated = ai.withUpdated(status, aiResponseText, errorMessage);
		aiRepository.save(updated); // id가 동일하면 update
	}

	public void sendSlackNotification(String slackId, String aiResponseText, String errorMessage) {
		try {
			SlackMessageCreateRequestDtoV1 slackRequestDto = SlackMessageCreateRequestDtoV1.builder()
				.recipientSlackId(slackId)
				.aiResponseText(aiResponseText)
				.errorMessage(errorMessage)
				.build();
			slackClient.dmToDliveryManager(slackRequestDto);
		} catch (Exception e) {
			log.error("[AiServiceV1] Slack 알림 실패: {}", e.getMessage());
			throw new AiException(AiError.AI_SLACK_NOTIFICATION_FAILED);
		}
	}
}
