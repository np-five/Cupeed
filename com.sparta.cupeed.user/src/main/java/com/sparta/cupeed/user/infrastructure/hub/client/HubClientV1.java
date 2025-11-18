package com.sparta.cupeed.user.infrastructure.hub.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.sparta.cupeed.user.infrastructure.hub.dto.response.HubGetResponseDtoV1;
import com.sparta.cupeed.user.infrastructure.hub.dto.response.HubInternalGetResponseDtoV1;

@FeignClient("hub")
public interface HubClientV1 {

	@GetMapping("/internal/v1/hubs/{hubName}")
	HubInternalGetResponseDtoV1 getInternalHubByName(@PathVariable String hubName);

	@GetMapping("/v1/hubs/{hubId}")
	HubGetResponseDtoV1 getHubById(@PathVariable UUID hubId);

}
