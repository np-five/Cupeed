package com.sparta.cupeed.company.infrastructure.hub.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.sparta.cupeed.company.infrastructure.hub.client.dto.response.HubInternalResponseDtoV1;

@FeignClient(name = "hub")
public interface HubClientV1 {

	// 허브 이름으로 허브 ID 조회
	@GetMapping("/internal/v1/hubs/{hubName}")
	HubInternalResponseDtoV1 getInternalHubIdByName(@PathVariable("hubName") String hubName);
}
