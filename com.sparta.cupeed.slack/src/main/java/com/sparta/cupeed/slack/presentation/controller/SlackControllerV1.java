package com.sparta.cupeed.slack.presentation.controller;

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
import org.springframework.web.bind.annotation.RestController;

import com.sparta.cupeed.slack.application.service.SlackServiceV1;
import com.sparta.cupeed.slack.presentation.dto.request.SlackDeliveryManagerDMCreateRequestDtoV1;
import com.sparta.cupeed.slack.presentation.dto.request.SlackReceiveCompanyDMCreateRequestDtoV1;
import com.sparta.cupeed.slack.presentation.dto.response.SlackCreateResponseDtoV1;
import com.sparta.cupeed.slack.presentation.dto.response.SlackGetResponseDtoV1;
import com.sparta.cupeed.slack.presentation.dto.response.SlacksGetResponseDtoV1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/slacks")
@Tag(name = "Slack-Message", description = "슬랙 메시지 전송 및 조회 API")
public class SlackControllerV1 {

	private final SlackServiceV1 slackService;

	@Operation(summary = "주문완료 DM 전송", description = "주문이 완료되면 수령업체에게 주문완료 알림을 보냅니다.")
	@PostMapping("/dm/toReceiveCompany")
	public ResponseEntity<SlackCreateResponseDtoV1> createDMToReciveCompany(
		@Valid @RequestBody SlackReceiveCompanyDMCreateRequestDtoV1 requestDto
	) {
		SlackCreateResponseDtoV1 response = slackService.createDMToReciveCompany(requestDto);
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "배송 요약메시지 DM 전송", description = "배송 담당자가 정해지면 Gemini가 생성한 배송 요약메시지를 발송 담당자에게 전송합니다.")
	@PostMapping("/dm/toDliveryManager")
	public ResponseEntity<SlackCreateResponseDtoV1> createDMToDliveryManager(
		@Valid @RequestBody SlackDeliveryManagerDMCreateRequestDtoV1 requestDto
	) {
		SlackCreateResponseDtoV1 response = slackService.createDMToDliveryManager(requestDto);
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "슬랙 메시지 상세 조회", description = "관리자가 슬랙 메시지를 상세 조회합니다.")
	@GetMapping("/{slackMessageId}")
	public ResponseEntity<SlackGetResponseDtoV1> getSlackMessage(
		@Parameter(description = "슬랙 메시지 ID", example = "b18d6d27-9a9e-4c6d-8db0-3aefb174edc1", required = true)
		@PathVariable("slackMessageId") UUID slackMessageId
	) {
		SlackGetResponseDtoV1 response = slackService.getSlackMessage(slackMessageId);
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "슬랙 메시지 목록 조회", description = "관리자가 슬랙 메시지 목록을 조회합니다.")
	@GetMapping
	public ResponseEntity<SlacksGetResponseDtoV1> getSlackMessages(
		@ParameterObject @PageableDefault(size = 5) Pageable pageable
	) {
		SlacksGetResponseDtoV1 response = slackService.getSlackMessages(pageable);
		return ResponseEntity.ok(response);
	}

}
