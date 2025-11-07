package com.sparta.cupeed.order.presentation.controller;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.cupeed.order.application.service.OrderServiceV1;
import com.sparta.cupeed.order.presentation.dto.request.OrderPostRequestDtoV1;
import com.sparta.cupeed.order.presentation.dto.request.OrderStatusUpdateRequestDtoV1;
import com.sparta.cupeed.order.presentation.dto.response.OrderPostResponseDtoV1;
import com.sparta.cupeed.order.presentation.dto.response.OrderGetResponseDtoV1;
import com.sparta.cupeed.order.presentation.dto.response.OrdersGetResponseDtoV1;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/orders")
public class OrderControllerV1 {

	private final OrderServiceV1 orderServiceV1;

	@PostMapping
	public ResponseEntity<OrderPostResponseDtoV1> createOrder(
		@RequestBody @Valid OrderPostRequestDtoV1 requestDto
	) {
		OrderPostResponseDtoV1 response = orderServiceV1.createOrder(requestDto);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{orderId}")
	public ResponseEntity<OrderGetResponseDtoV1> getOrder(
		@PathVariable("orderId") UUID orderId
	) {
		OrderGetResponseDtoV1 response = orderServiceV1.getOrder(orderId);
		return ResponseEntity.ok(response);
	}

	@PutMapping("/{orderId}")
	public ResponseEntity<OrderPostResponseDtoV1> updateOrder(
		@PathVariable("orderId") UUID orderId,
		@RequestBody @Valid OrderPostRequestDtoV1 requestDto
	) {
		OrderPostResponseDtoV1 response = orderServiceV1.updateOrder(orderId, requestDto);
		return ResponseEntity.ok(response);
	}

	@PatchMapping("/{orderId}/status")
	public ResponseEntity<OrderPostResponseDtoV1> updateOrderStatus(
		@PathVariable UUID orderId,
		@RequestBody @Valid OrderStatusUpdateRequestDtoV1 requestDto
	) {
		OrderPostResponseDtoV1 response = orderServiceV1.updateOrderStatus(orderId, requestDto);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/{orderId}/cancel")
	public ResponseEntity<Void> cancelOrder(
		@PathVariable UUID orderId
	) {
		orderServiceV1.cancelOrder(orderId);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{orderId}")
	public ResponseEntity<Void> deleteOrder(
		@PathVariable UUID orderId
	) {
		orderServiceV1.deleteOrder(orderId);
		return ResponseEntity.noContent().build();
	}

	@GetMapping
	public ResponseEntity<OrdersGetResponseDtoV1> getOrders(@PageableDefault(size = 5) Pageable pageable) {
		OrdersGetResponseDtoV1 response = orderServiceV1.getOrders(pageable);
		return ResponseEntity.ok(response);
	}
}
