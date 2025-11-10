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
import com.sparta.cupeed.ai.infrastructure.slack.client.SlackClientV1;
import com.sparta.cupeed.ai.infrastructure.slack.dto.SlackMessageCreateRequestDtoV1;
import com.sparta.cupeed.ai.presentation.dto.response.AiHistoriesGetResponseDtoV1;
import com.sparta.cupeed.ai.presentation.dto.response.AiHistoryGetResponseDtoV1;
import com.sparta.cupeed.ai.presentation.dto.response.AiTextCreateResponseDtoV1;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AiServiceV1 {

	private final PromptBuilder promptBuilder;
	private final GeminiAPIClientV1 geminiAPIClient;
	private final AiRepository aiRepository;
	private final SlackClientV1 slackClient;

	@Transactional
	public AiTextCreateResponseDtoV1 createAiText(GeminiSendRequestDtoV1 requestDto) {
		String prompt = promptBuilder.generateAiTextPrompt(requestDto);

		Ai.Status status;
		String aiResponseText = null;
		String errorMessage = null;

		try {
			aiResponseText = geminiAPIClient.createAiText(prompt);

			if (aiResponseText == null || aiResponseText.isBlank()) {
				status = Ai.Status.FAILED;
				errorMessage = "AI로부터 유효한 응답을 받지 못했습니다.";
			} else {
				status = Ai.Status.SUCCESS;
			}
		} catch (Exception e) {
			status = Ai.Status.FAILED;
			errorMessage = "AI 요청 중 오류 발생: " + e.getMessage();
		}

		Ai created = Ai.builder()
			.orderId(requestDto.getOrderId())
			.aiResponseText(aiResponseText)
			.status(status)
			.errorMessage(errorMessage)
			.createdAt(Instant.now())
			.build();

		Ai saved = aiRepository.save(created);

		// TODO : 슬랙에 aiText 전달
		slackClient.dmToDliveryManager(
			SlackMessageCreateRequestDtoV1.builder()
				.recipientSlackId("U09SFAT4V5E") // 임시 수령자 슬랙 ID - 차초희 멤버 ID
				.aiResponseText(aiResponseText)
				.errorMessage(errorMessage)
				.build()
		);

		return AiTextCreateResponseDtoV1.of(saved);
	}

	public AiHistoryGetResponseDtoV1 getAiHistory(UUID aiRequestId) {
		Ai aiHistory = aiRepository.findById(aiRequestId)
				.orElseThrow(() -> new IllegalArgumentException("AI 요청 내역을 찾을 수 없습니다."));
		return AiHistoryGetResponseDtoV1.of(aiHistory);
	}

	public AiHistoriesGetResponseDtoV1 getAiHistories(Pageable pageable) {
		Page<Ai> aiHistories = aiRepository.findAll(pageable);
		return AiHistoriesGetResponseDtoV1.of(aiHistories);
	}
}
