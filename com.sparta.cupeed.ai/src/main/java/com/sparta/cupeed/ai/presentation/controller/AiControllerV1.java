package com.sparta.cupeed.ai.presentation.controller;

import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.cupeed.ai.application.service.AiServiceV1;
import com.sparta.cupeed.ai.infrastructure.resttemplate.geminiapi.dto.GeminiSendRequestDtoV1;
import com.sparta.cupeed.ai.presentation.dto.response.AiHistoriesGetResponseDtoV1;
import com.sparta.cupeed.ai.presentation.dto.response.AiHistoryGetResponseDtoV1;
import com.sparta.cupeed.ai.presentation.dto.response.AiTextCreateResponseDtoV1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/ai-requests")
@Tag(name = "AI-Request", description = "AI 응답 생성 및 요청내역 조회 API")
public class AiControllerV1 {

	private final AiServiceV1 aiServiceV1;

	@Operation(summary = "Gemini 응답텍스트 생성", description = "주문 정보를 기반으로 발송 담당자에게 보낼 배송 요약메시지를 생성합니다.")
	@PostMapping
	public ResponseEntity<AiTextCreateResponseDtoV1> createAiText(
		@RequestBody @Valid GeminiSendRequestDtoV1 requestDto
	) {
		AiTextCreateResponseDtoV1 response = aiServiceV1.createAiText(requestDto);
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "AI 요청내역 상세 조회", description = "관리자가 AI 요청내역을 상세 조회합니다.")
	@GetMapping("/{aiRequestId}")
	public ResponseEntity<AiHistoryGetResponseDtoV1> getAiHistory(
		@Parameter(description = "AI 요청 ID", example = "b18d6d27-9a9e-4c6d-8db0-3aefb174edc1", required = true)
		@PathVariable("aiRequestId") UUID aiRequestId) {
		AiHistoryGetResponseDtoV1 response = aiServiceV1.getAiHistory(aiRequestId);
		return ResponseEntity.ok(response);
	}

	// 쿼리 DSL 적용
	@Operation(summary = "AI 요청내역 목록 조회", description = "관리자가 AI 요청내역 목록을 조회합니다.")
	@GetMapping
	public ResponseEntity<AiHistoriesGetResponseDtoV1> getAiHistories(
		@Parameter @RequestParam(required = false) String keyword,
		@ParameterObject @PageableDefault(size = 5) Pageable pageable
	) {
		AiHistoriesGetResponseDtoV1 response = aiServiceV1.getAiHistories(keyword, pageable);
		return ResponseEntity.ok(response);
	}
}
