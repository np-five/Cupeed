package com.sparta.cupeed.delivery.infrastructure.ai.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.sparta.cupeed.delivery.infrastructure.ai.dto.HubResponseDtoV1;

//도착 허브
@FeignClient(name = "hub-service", url = "${hub.service.url:http://localhost:8081}")
public interface HubClientV1 {

	@GetMapping("/v1/hubs/{hubId}")
	HubResponseDtoV1 getHubById(@PathVariable("hubId") UUID hubId);
}
