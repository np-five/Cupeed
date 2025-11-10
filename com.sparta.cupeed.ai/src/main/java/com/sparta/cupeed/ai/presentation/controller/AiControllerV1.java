package com.sparta.cupeed.ai.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.cupeed.ai.application.service.AiServiceV1;
import com.sparta.cupeed.ai.infrastructure.resttemplate.geminiapi.dto.GeminiSendRequestDtoV1;
import com.sparta.cupeed.ai.presentation.dto.response.AiCreateResponseDtoV1;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/ai-requests")
public class AiControllerV1 {

	private final AiServiceV1 aiServiceV1;

	@PostMapping
	public ResponseEntity<AiCreateResponseDtoV1> createAiText(
		@Valid @RequestBody GeminiSendRequestDtoV1 requestDto
	) {
		AiCreateResponseDtoV1 response = aiServiceV1.createAiText(requestDto);
		return ResponseEntity.ok(response);
	}
}
