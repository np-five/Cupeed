package com.sparta.cupeed.user.infrastructure.hub.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.sparta.cupeed.user.infrastructure.hub.dto.response.HubGetResponseDtoV1;

@FeignClient("hub")
public interface HubClientV1 {

	@GetMapping("/v1/hubs/by-name")
	HubGetResponseDtoV1 getHubByName(@RequestParam String name);

	@GetMapping("/v1/hubs/{hubId}")
	HubGetResponseDtoV1 getHubById(@PathVariable UUID hubId);

}
