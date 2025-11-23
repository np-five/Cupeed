package com.sparta.cupeed.order.infrastructure.delivery.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.sparta.cupeed.order.infrastructure.config.FeignClientConfig;
import com.sparta.cupeed.order.infrastructure.delivery.dto.request.DeliveryCreateRequestDtoV1;

@FeignClient(name = "delivery", configuration = FeignClientConfig.class)
public interface DeliveryClientV1 {
	@PostMapping("/v1/deliveries")
	void createDelivery(
		@RequestBody DeliveryCreateRequestDtoV1 requestDto,
		@RequestHeader("X-User-Name") String username
	);
}
