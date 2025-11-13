package com.sparta.cupeed.product.infrastructure.user.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user")
public interface UserClientV1 {

	@GetMapping("/internal/v1/users/{userId}")
	UUID getInternalUser(@PathVariable UUID userId);
}
