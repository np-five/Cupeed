package com.sparta.cupeed.ai.application.service;

import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.cupeed.ai.domain.model.Ai;
import com.sparta.cupeed.ai.domain.repository.AiRepository;
import com.sparta.cupeed.ai.infrastructure.resttemplate.geminiapi.client.GeminiAPIClientV1;
import com.sparta.cupeed.ai.infrastructure.resttemplate.geminiapi.dto.GeminiSendRequestDtoV1;
import com.sparta.cupeed.ai.infrastructure.resttemplate.geminiapi.prompt.PromptBuilder;
import com.sparta.cupeed.ai.presentation.dto.response.AiCreateResponseDtoV1;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AiServiceV1 {

	private final PromptBuilder promptBuilder;
	private final GeminiAPIClientV1 geminiAPIClient;
	private final AiRepository aiRepository;

	@Transactional
	public AiCreateResponseDtoV1 createAiText(GeminiSendRequestDtoV1 requestDto) {
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

		return AiCreateResponseDtoV1.of(saved);
	}
}
