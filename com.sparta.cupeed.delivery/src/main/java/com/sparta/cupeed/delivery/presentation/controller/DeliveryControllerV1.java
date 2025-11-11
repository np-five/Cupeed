package com.sparta.cupeed.delivery.presentation.controller;

import com.sparta.cupeed.delivery.presentation.dto.*;
import com.sparta.cupeed.delivery.application.service.DeliveryServiceV1;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/deliveries")
public class DeliveryControllerV1 {

	private final DeliveryServiceV1 deliveryServiceV1;

	public DeliveryControllerV1(DeliveryServiceV1 deliveryServiceV1) {
		this.deliveryServiceV1 = deliveryServiceV1;
	}

	//배송 생성
	@PostMapping
	public ResponseEntity<DeliveryResponseDtoV1> createDelivery(
		@RequestBody DeliveryCreateRequestDtoV1 request,
		@RequestHeader(value = "X-User-Name", required = false) String username
	) {
		validateAuthentication(username);

		DeliveryResponseDtoV1 response = deliveryServiceV1.createDelivery(request, username);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	//배송 전체 조회
	@GetMapping
	public ResponseEntity<DeliveriesResponseDtoV1> getDeliveries(
		@PageableDefault(size = 10) Pageable pageable,
		@RequestHeader(value = "X-User-Id", required = false) String userId
	) {
		validateAuthentication(userId);
		// Pageable을 int로 변환
		DeliveriesResponseDtoV1 response = deliveryServiceV1.getDeliveries(
			pageable.getPageNumber(),
			pageable.getPageSize()
		);
		return ResponseEntity.ok(response);
	}

	//배송 단건 조회
	@GetMapping("/{deliveryId}")
	public ResponseEntity<DeliveryResponseDtoV1> getDelivery(
		@PathVariable UUID deliveryId,
		@RequestHeader(value = "X-User-Id", required = false) String userId
	) {
		validateAuthentication(userId);

		DeliveryResponseDtoV1 response = deliveryServiceV1.getDelivery(deliveryId);
		return ResponseEntity.ok(response);
	}

	//배송 상태 변경
	@PatchMapping("/{deliveryId}/status")
	public ResponseEntity<DeliveryResponseDtoV1> updateDeliveryStatus(
		@PathVariable UUID deliveryId,
		@RequestBody DeliveryStatusUpdateRequestDtoV1 request,
		@RequestHeader(value = "X-User-Name", required = false) String username
	) {
		validateAuthentication(username);

		DeliveryResponseDtoV1 response = deliveryServiceV1.updateDeliveryStatus(deliveryId, request, username);
		return ResponseEntity.ok(response);
	}

	//배송 삭제
	@DeleteMapping("/{deliveryId}")
	public ResponseEntity<Void> deleteDelivery(
		@PathVariable UUID deliveryId,
		@RequestHeader(value = "X-User-Name", required = false) String username
	) {
		validateAuthentication(username);

		deliveryServiceV1.deleteDelivery(deliveryId, username);
		return ResponseEntity.noContent().build();
	}

	//인증 정보 검증
	private void validateAuthentication(String authInfo) {
		if (authInfo == null || authInfo.isEmpty()) {
			throw new IllegalArgumentException("인증 정보가 없습니다.");
		}
	}
}