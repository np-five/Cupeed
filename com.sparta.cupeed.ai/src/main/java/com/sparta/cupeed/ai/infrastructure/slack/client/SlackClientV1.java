package com.sparta.cupeed.ai.infrastructure.slack.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "slack")
public interface SlackClientV1 {
	@PostMapping("v1/slacks/dm/toDliveryManager")
	void dmToDliveryManager(String aiResponseText, UUID recipientId);
}
