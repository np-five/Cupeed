package com.sparta.cupeed.order.infrastructure.delivery.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "delivery")
public interface DeliveryClientV1 {
	@PostMapping("/v1/deliveries")
	void createDelivery(@RequestParam UUID orderId, @RequestParam UUID companyId);
}
