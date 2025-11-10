package com.sparta.cupeed.slack.presentation.controller;

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

import com.sparta.cupeed.slack.application.service.SlackServiceV1;
import com.sparta.cupeed.slack.presentation.dto.request.SlackDeliveryManagerDMCreateRequestDtoV1;
import com.sparta.cupeed.slack.presentation.dto.request.SlackReceiveCompanyDMCreateRequestDtoV1;
import com.sparta.cupeed.slack.presentation.dto.response.SlackCreateResponseDtoV1;
import com.sparta.cupeed.slack.presentation.dto.response.SlackGetResponseDtoV1;
import com.sparta.cupeed.slack.presentation.dto.response.SlacksGetResponseDtoV1;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/slacks")
public class SlackControllerV1 {

	private final SlackServiceV1 slackService;

	// 수령업체에게 주문완료 DM 전송
	@PostMapping("/dm/toReciveCompany")
	public ResponseEntity<SlackCreateResponseDtoV1> createDMToReciveCompany(
		@Valid @RequestBody SlackReceiveCompanyDMCreateRequestDtoV1 requestDto
	) {
		SlackCreateResponseDtoV1 response = slackService.createDMToReciveCompany(requestDto);
		return ResponseEntity.ok(response);
	}

	// 배송 담당자에게 AI응답 DM 전송
	@PostMapping("/dm/toDliveryManager")
	public ResponseEntity<SlackCreateResponseDtoV1> createDMToDliveryManager(
		@Valid @RequestBody SlackDeliveryManagerDMCreateRequestDtoV1 requestDto
	) {
		SlackCreateResponseDtoV1 response = slackService.createDMToDliveryManager(requestDto);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{slackMessageId}")
	public ResponseEntity<SlackGetResponseDtoV1> getSlackMessage(
		@Valid @PathVariable("slackMessageId") UUID slackMessageId
	) {
		SlackGetResponseDtoV1 response = slackService.getSlackMessage(slackMessageId);
		return ResponseEntity.ok(response);
	}

	@GetMapping
	public ResponseEntity<SlacksGetResponseDtoV1> getSlackMessages(
		@PageableDefault(size = 5) Pageable pageable
	) {
		SlacksGetResponseDtoV1 response = slackService.getSlackMessages(pageable);
		return ResponseEntity.ok(response);
	}

}
