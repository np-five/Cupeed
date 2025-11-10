package com.sparta.cupeed.hubroute.infrastructure.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "hub")
public interface HubClient {

	@GetMapping("/v1/hubs/{hubId}/exists")
	boolean checkHubExists(@PathVariable("hubId") UUID hubId);

}
