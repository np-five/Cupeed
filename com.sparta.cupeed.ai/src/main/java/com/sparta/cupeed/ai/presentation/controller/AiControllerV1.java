package com.sparta.cupeed.ai.presentation.controller;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.cupeed.ai.application.service.AiServiceV1;
import com.sparta.cupeed.ai.infrastructure.resttemplate.geminiapi.dto.GeminiSendRequestDtoV1;
import com.sparta.cupeed.ai.presentation.dto.response.AiHistoriesGetResponseDtoV1;
import com.sparta.cupeed.ai.presentation.dto.response.AiHistoryGetResponseDtoV1;
import com.sparta.cupeed.ai.presentation.dto.response.AiTextCreateResponseDtoV1;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/ai-requests")
public class AiControllerV1 {

	private final AiServiceV1 aiServiceV1;

	@PostMapping
	public ResponseEntity<AiTextCreateResponseDtoV1> createAiText(
		@Valid @RequestBody GeminiSendRequestDtoV1 requestDto
	) {
		AiTextCreateResponseDtoV1 response = aiServiceV1.createAiText(requestDto);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{aiRequestId}")
	public ResponseEntity<AiHistoryGetResponseDtoV1> getAiHistory(@PathVariable("aiRequestId") UUID aiRequestId) {
		AiHistoryGetResponseDtoV1 response = aiServiceV1.getAiHistory(aiRequestId);
		return ResponseEntity.ok(response);
	}

	@GetMapping
	public ResponseEntity<AiHistoriesGetResponseDtoV1> getAiHistories(
		@PageableDefault(size = 5) Pageable pageable
	) {
		AiHistoriesGetResponseDtoV1 resopnse = aiServiceV1.getAiHistories(pageable);
		return ResponseEntity.ok(resopnse);
	}
}
