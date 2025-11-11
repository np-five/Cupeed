package com.sparta.cupeed.hubroute.infrastructure.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.sparta.cupeed.hubroute.infrastructure.dto.HubResponseDto;

@FeignClient(name = "hub")
public interface HubClient {

	@GetMapping("/v1/hubs/{hubId}/exists")
	boolean checkHubExists(@PathVariable("hubId") UUID hubId);

	@GetMapping("/v1/hubs/{hubId}")
	HubResponseDto getHubById(@PathVariable("hubId") UUID hubId);
}
