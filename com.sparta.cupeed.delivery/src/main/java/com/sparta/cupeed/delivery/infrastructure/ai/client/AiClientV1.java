package com.sparta.cupeed.delivery.infrastructure.ai.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.sparta.cupeed.delivery.infrastructure.ai.dto.GeminiSendRequestDtoV1;

@FeignClient(name = "ai")
public interface AiClientV1 {
	@PostMapping("/v1/ai-requests")
	void createAiText(@RequestBody GeminiSendRequestDtoV1 requestDto);
}
