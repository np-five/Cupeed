package com.sparta.cupeed.ai.infrastructure.slack.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.sparta.cupeed.ai.infrastructure.config.FeignClientConfig;
import com.sparta.cupeed.ai.infrastructure.slack.dto.SlackMessageCreateRequestDtoV1;

@FeignClient(
	name = "slack",
	configuration = FeignClientConfig.class
)
public interface SlackClientV1 {
	@PostMapping("v1/slacks/dm/toDliveryManager")
	void dmToDliveryManager(@RequestBody SlackMessageCreateRequestDtoV1 requestDto);
}
