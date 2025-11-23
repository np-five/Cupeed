package com.sparta.cupeed.order.infrastructure.slack.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.sparta.cupeed.order.infrastructure.config.FeignClientAsyncConfig;
import com.sparta.cupeed.order.infrastructure.config.FeignClientConfig;
import com.sparta.cupeed.order.infrastructure.slack.dto.request.SlackMessageCreateRequestDtoV1;

@FeignClient(name = "slack", configuration = FeignClientAsyncConfig.class)
public interface SlackClientV1 {
	@PostMapping("/v1/slacks/dm/toReceiveCompany")
	void dmToReceiveCompany(@RequestBody SlackMessageCreateRequestDtoV1 dto);
}
