package com.sparta.cupeed.slack.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.cupeed.slack.application.service.SlackServiceV1;
import com.sparta.cupeed.slack.infrastructure.resttemplate.slackapi.dto.SlackSendRequestDtoV1;
import com.sparta.cupeed.slack.presentation.dto.response.SlackCreateResponseDtoV1;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/slacks")
public class SlackControllerV1 {

	private final SlackServiceV1 slackServiceV1;

	@PostMapping("/dm")
	public ResponseEntity<SlackCreateResponseDtoV1> createSlackMessageDM(
		@Valid @RequestBody SlackSendRequestDtoV1 requestDto
	) {
		SlackCreateResponseDtoV1 response = slackServiceV1.createSlackMessage(requestDto);
		return ResponseEntity.ok(response);
	}

	// @GetMapping("/{slackMessageId}")
	// public ResponseEntity<SlackGetResponseDtoV1> getSlackMessage(
	// 	@Valid @PathVariable("slackMessageId") String slackMessageId
	// ) {
	// 	SlackGetResponseDtoV1 response = slackServiceV1.getSlackMessage(slackMessageId);
	// 	return ResponseEntity.ok(response);
	// }
	//
	// @GetMapping
	// public ResponseEntity<SlacksGetResponseDtoV1> getSlackMessages(
	// 	@PageableDefault Pageable pageable
	// ) {
	// 	SlacksGetResponseDtoV1 response = slackServiceV1.getSlackMessages(pageable);
	// 	return ResponseEntity.ok(response);
	// }

}
