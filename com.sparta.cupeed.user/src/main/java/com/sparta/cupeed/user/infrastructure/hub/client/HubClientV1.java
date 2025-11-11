package com.sparta.cupeed.user.infrastructure.hub.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("hub")
public interface HubClientV1 {

	@GetMapping("/internal/v1/hubs")
	UUID getHubByName(@RequestParam String name);
}
