package com.sparta.cupeed.ai.application.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.cupeed.ai.domain.model.Ai;
import com.sparta.cupeed.ai.domain.repository.AiRepository;
import com.sparta.cupeed.ai.infrastructure.resttemplate.geminiapi.dto.GeminiSendRequestDtoV1;
import com.sparta.cupeed.ai.infrastructure.resttemplate.geminiapi.prompt.PromptBuilder;
import com.sparta.cupeed.ai.infrastructure.security.auth.RoleEnum;
import com.sparta.cupeed.ai.infrastructure.security.auth.UserDetailsImpl;
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
	private final AiRepository aiRepository;
	private final AiAsyncServiceV1 aiAsyncService;

	@Transactional(noRollbackFor = AiException.class)
	public AiTextCreateResponseDtoV1 createAiText(UserDetailsImpl userDetails, GeminiSendRequestDtoV1 requestDto) {
		String prompt = promptBuilder.generateAiTextPrompt(requestDto);
		if (prompt == null || prompt.isBlank()) {
			throw new AiException(AiError.AI_PROMPT_BUILD_FAILED);
		}
		Ai ai = Ai.builder()
			.orderId(requestDto.getOrderId())
			.status(Ai.Status.PENDING)
			.build();
		Ai saved = aiRepository.save(ai);
		aiAsyncService.processGeminiApiAsync(saved.getId(), prompt, userDetails.getSlackId());
		return AiTextCreateResponseDtoV1.of(saved);
	}

	@PreAuthorize("hasAnyAuthority('ROLE_MASTER')")
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

	@PreAuthorize("hasAnyAuthority('ROLE_MASTER')")
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
