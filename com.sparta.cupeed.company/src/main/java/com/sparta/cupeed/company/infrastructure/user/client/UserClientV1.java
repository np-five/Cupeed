package com.sparta.cupeed.company.infrastructure.user.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.sparta.cupeed.company.infrastructure.user.dto.response.InternalUserResponseDtoV1;

@FeignClient(name = "user")
public interface UserClientV1 {

	@GetMapping("/internal/v1/users/{userId}")
	InternalUserResponseDtoV1 getInternalUser(@PathVariable UUID userId);
}
