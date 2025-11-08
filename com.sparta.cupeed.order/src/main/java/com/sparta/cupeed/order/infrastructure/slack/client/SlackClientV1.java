package com.sparta.cupeed.order.infrastructure.slack.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import com.sparta.cupeed.order.infrastructure.slack.dto.request.SlackMessageCreateRequestDtoV1;

@FeignClient(name = "slack")
public interface SlackClientV1 {
	@PostMapping("/v1/slacks")
	void sendMessage(SlackMessageCreateRequestDtoV1 slackRequestDto);
}
