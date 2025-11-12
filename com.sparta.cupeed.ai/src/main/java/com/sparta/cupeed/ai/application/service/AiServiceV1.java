package com.sparta.cupeed.ai.application.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.cupeed.ai.domain.model.Ai;
import com.sparta.cupeed.ai.domain.repository.AiRepository;
import com.sparta.cupeed.ai.infrastructure.resttemplate.geminiapi.client.GeminiAPIClientV1;
import com.sparta.cupeed.ai.infrastructure.resttemplate.geminiapi.dto.GeminiSendRequestDtoV1;
import com.sparta.cupeed.ai.infrastructure.resttemplate.geminiapi.prompt.PromptBuilder;
import com.sparta.cupeed.ai.infrastructure.security.RoleEnum;
import com.sparta.cupeed.ai.infrastructure.security.auth.UserDetailsImpl;
import com.sparta.cupeed.ai.infrastructure.slack.client.SlackClientV1;
import com.sparta.cupeed.ai.infrastructure.slack.dto.SlackMessageCreateRequestDtoV1;
import com.sparta.cupeed.ai.presentation.advice.AiError;
import com.sparta.cupeed.ai.presentation.advice.AiException;
import com.sparta.cupeed.ai.presentation.dto.response.AiHistoriesGetResponseDtoV1;
import com.sparta.cupeed.ai.presentation.dto.response.AiHistoryGetResponseDtoV1;
import com.sparta.cupeed.ai.presentation.dto.response.AiTextCreateResponseDtoV1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiServiceV1 {

	private final PromptBuilder promptBuilder;
	private final GeminiAPIClientV1 geminiAPIClient;
	private final AiRepository aiRepository;
	private final SlackClientV1 slackClient;

	@Transactional(noRollbackFor = AiException.class)
	public AiTextCreateResponseDtoV1 createAiText(GeminiSendRequestDtoV1 requestDto) {
		String prompt = promptBuilder.generateAiTextPrompt(requestDto);
		String errorMessage = null;
		String aiResponseText = null;
		Ai.Status status;

		if (prompt == null || prompt.isBlank()) {
			status = Ai.Status.FAILED;
			errorMessage = AiError.AI_PROMPT_BUILD_FAILED.getErrorMessage();
		} else {
			// Gemini API 통신
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

			Ai created = Ai.builder()
				.orderId(requestDto.getOrderId())
				.aiResponseText(aiResponseText)
				.status(status)
				.errorMessage(errorMessage)
				.createdAt(Instant.now())
				.build();
			Ai saved = aiRepository.save(created);

			// Cupeed 슬랙 통신
			try {
				slackClient.dmToDliveryManager(
					SlackMessageCreateRequestDtoV1.builder()
						.recipientSlackId("U09SFAT4V5E")
						.aiResponseText(aiResponseText)
						.errorMessage(errorMessage)
						.build()
				);
			} catch (Exception e) {
				throw new AiException(AiError.AI_SLACK_NOTIFICATION_FAILED);
			}

			return AiTextCreateResponseDtoV1.of(saved);
		}

		// prompt가 없는 경우 DB에 기록
		Ai created = Ai.builder()
			.orderId(requestDto.getOrderId())
			.aiResponseText(aiResponseText)
			.status(status)
			.errorMessage(errorMessage)
			.createdAt(Instant.now())
			.build();
		Ai saved = aiRepository.save(created);
		return AiTextCreateResponseDtoV1.of(saved);
	}

	@Transactional(readOnly = true)
	public AiHistoryGetResponseDtoV1 getAiHistory(UserDetailsImpl userDetails, UUID aiRequestId) {
		Ai aiHistory = aiRepository.findById(aiRequestId)
				.orElseThrow(() -> new AiException(AiError.AI_HISTORY_NOT_FOUND));
		RoleEnum role = RoleEnum.fromAuthority(userDetails.getRole());
		if (role != RoleEnum.MASTER) {
			throw new AiException(AiError.AI_ACCESS_DENIED);
		}
		return AiHistoryGetResponseDtoV1.of(aiHistory);
	}

	@Transactional(readOnly = true)
	public AiHistoriesGetResponseDtoV1 getAiHistories(UserDetailsImpl userDetails, String keyword, Pageable pageable) {
		Page<Ai> aiHistories = aiRepository.searchAiHistories(keyword, pageable);
		RoleEnum role = RoleEnum.fromAuthority(userDetails.getRole());
		if (role != RoleEnum.MASTER) {
			throw new AiException(AiError.AI_ACCESS_DENIED);
		}
		return AiHistoriesGetResponseDtoV1.of(aiHistories);
	}
}
