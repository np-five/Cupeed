package com.sparta.cupeed.order.presentation.controller;

import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.cupeed.order.application.service.OrderServiceV1;
import com.sparta.cupeed.order.presentation.dto.request.OrderPostRequestDtoV1;
import com.sparta.cupeed.order.presentation.dto.request.OrderStatusUpdateRequestDtoV1;
import com.sparta.cupeed.order.presentation.dto.response.OrderPostResponseDtoV1;
import com.sparta.cupeed.order.presentation.dto.response.OrderGetResponseDtoV1;
import com.sparta.cupeed.order.presentation.dto.response.OrdersGetResponseDtoV1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/orders")
@Tag(name = "Order", description = "주문 관련 API")
public class OrderControllerV1 {

	private final OrderServiceV1 orderServiceV1;

	@Operation(summary = "주문 생성", description = "새로운 주문을 생성합니다.")
	@PostMapping
	public ResponseEntity<OrderPostResponseDtoV1> createOrder(
		@RequestBody @Valid OrderPostRequestDtoV1 requestDto
	) {
		OrderPostResponseDtoV1 response = orderServiceV1.createOrder(requestDto);
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "주문 상세 조회", description = "주문 ID로 주문을 조회합니다.")
	@GetMapping("/{orderId}")
	public ResponseEntity<OrderGetResponseDtoV1> getOrder(
		@Parameter(description = "조회할 주문 ID", example = "b18d6d27-9a9e-4c6d-8db0-3aefb174edc1", required = true)
		@PathVariable("orderId") UUID orderId
	) {
		OrderGetResponseDtoV1 response = orderServiceV1.getOrder(orderId);
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "주문 전체 수정", description = "주문 정보를 전체 업데이트합니다.")
	@PutMapping("/{orderId}")
	public ResponseEntity<OrderPostResponseDtoV1> updateOrder(
		@Parameter(description = "수정할 주문 ID", example = "b18d6d27-9a9e-4c6d-8db0-3aefb174edc1", required = true)
		@PathVariable("orderId") UUID orderId,
		@RequestBody @Valid OrderPostRequestDtoV1 requestDto
	) {
		OrderPostResponseDtoV1 response = orderServiceV1.updateOrder(orderId, requestDto);
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "주문 상태 수정", description = "주문의 상태만 부분적으로 업데이트합니다.")
	@PatchMapping("/{orderId}/status")
	public ResponseEntity<OrderPostResponseDtoV1> updateOrderStatus(
		@Parameter(description = "수정할 주문 ID", example = "b18d6d27-9a9e-4c6d-8db0-3aefb174edc1", required = true)
		@PathVariable UUID orderId,
		@RequestBody @Valid OrderStatusUpdateRequestDtoV1 requestDto
	) {
		OrderPostResponseDtoV1 response = orderServiceV1.updateOrderStatus(orderId, requestDto);
		return ResponseEntity.ok(response);
	}

	// 응답 수정할 예정
	@Operation(summary = "주문 취소", description = "주문을 취소합니다.")
	@PostMapping("/{orderId}/cancel")
	public ResponseEntity<Void> cancelOrder(
		@Parameter(description = "취소할 주문 ID", example = "b18d6d27-9a9e-4c6d-8db0-3aefb174edc1", required = true)
		@PathVariable UUID orderId
	) {
		orderServiceV1.cancelOrder(orderId);
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "주문 삭제", description = "주문을 삭제합니다. 성공 시 204 No Content 반환.")
	@DeleteMapping("/{orderId}")
	public ResponseEntity<Void> deleteOrder(
		@Parameter(description = "삭제할 주문 ID", example = "b18d6d27-9a9e-4c6d-8db0-3aefb174edc1", required = true)
		@PathVariable UUID orderId
	) {
		orderServiceV1.deleteOrder(orderId);
		return ResponseEntity.noContent().build();
	}

	// 쿼리 DSL 적용
	@Operation(summary = "주문 목록 조회", description = "페이지 단위로 주문 목록을 조회합니다.")
	@GetMapping
	public ResponseEntity<OrdersGetResponseDtoV1> getOrders(
		@Parameter(description = "검색 키워드 : 주문 번호, 수령업체명") @RequestParam(required = false) String keyword,
		@ParameterObject @PageableDefault(size = 5) Pageable pageable
	) {
		OrdersGetResponseDtoV1 response = orderServiceV1.getOrders(keyword, pageable);
		return ResponseEntity.ok(response);
	}
}
