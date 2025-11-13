package com.sparta.cupeed.delivery.infrastructure.ai.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.sparta.cupeed.delivery.infrastructure.ai.dto.OrderResponseDtoV1;

//출발 허브
@FeignClient(name = "order-service", url = "${order.service.url:http://localhost:8080}")
public interface OrderClientV1 {

	@GetMapping("/v1/orders/{orderId}")
	OrderResponseDtoV1 getOrderById(@PathVariable("orderId") UUID orderId);
}