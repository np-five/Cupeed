package com.sparta.cupeed.order.presentation.controller;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.cupeed.order.application.service.OrderServiceV1;
import com.sparta.cupeed.order.presentation.dto.request.OrderCreateRequestDtoV1;
import com.sparta.cupeed.order.presentation.dto.response.OrderCreateResponseDtoV1;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/orders")
public class OrderControllerV1 {

	private final OrderServiceV1 orderServiceV1;

	@PostMapping
	public ResponseEntity<OrderCreateResponseDtoV1> createOrder(
		@RequestBody @Valid OrderCreateRequestDtoV1 requestDto
	) {
		OrderCreateResponseDtoV1 response = orderServiceV1.createOrder(requestDto);
		return ResponseEntity.ok(response);
	}

	// @GetMapping
	// public ResponseEntity<> getOrders() {
	// 	return ResponseEntity.ok();
	// }
	//
	// @GetMapping("/{orderId}")
	// public ResponseEntity<> getOrder() {
	// 	return ResponseEntity.ok();
	// }
	//
	// @PutMapping("/{orderId}")
	// public ResponseEntity<> updateOrder() {
	//
	// }
	//
	// @DeleteMapping("/{orderId}")
	// public ResponseEntity<> deleteOrder() {
	//
	// }
	//
	// @PostMapping("/{orderId}")
	// public ResponseEntity<> processOrder() {
	//
	// }
}
