package com.sparta.cupeed.delivery.presentation.controller;

import com.sparta.cupeed.delivery.presentation.dto.DeliveryManagerCreateRequestDtoV1;
import com.sparta.cupeed.delivery.presentation.dto.DeliveryManagerResponseDtoV1;
import com.sparta.cupeed.delivery.presentation.dto.*;
import com.sparta.cupeed.delivery.domain.model.DeliveryType;
import com.sparta.cupeed.delivery.application.service.DeliveryManagerServiceV1;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/delivery-managers")
public class DeliveryManagerControllerV1 {

	private final DeliveryManagerServiceV1 deliveryManagerServiceV1;

	public DeliveryManagerControllerV1(DeliveryManagerServiceV1 deliveryManagerServiceV1) {
		this.deliveryManagerServiceV1 = deliveryManagerServiceV1;
	}

	//배송 담당자 생성
	@PostMapping
	public ResponseEntity<DeliveryManagerResponseDtoV1> createDeliveryManager(
		@RequestBody DeliveryManagerCreateRequestDtoV1 request,
		@RequestHeader(value = "X-User-Name", required = false) String username
	) {
		validateAuthentication(username);

		DeliveryManagerResponseDtoV1 response = deliveryManagerServiceV1.createDeliveryManager(request, username);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	//배송 담당자 전체 조회
	@GetMapping
	public ResponseEntity<DeliveryManagersResponseDtoV1> getDeliveryManagers(
		@PageableDefault(size = 10) Pageable pageable,
		@RequestHeader(value = "X-User-Id", required = false) String userId
	) {
		validateAuthentication(userId);

		DeliveryManagersResponseDtoV1 response = deliveryManagerServiceV1.getDeliveryManagers(pageable);
		return ResponseEntity.ok(response);
	}

	//배송 담당자 단건 조회
	@GetMapping("/{managerId}")
	public ResponseEntity<DeliveryManagerResponseDtoV1> getDeliveryManager(
		@PathVariable UUID managerId,
		@RequestHeader(value = "X-User-Id", required = false) String userId
	) {
		validateAuthentication(userId);

		DeliveryManagerResponseDtoV1 response = deliveryManagerServiceV1.getDeliveryManager(managerId);
		return ResponseEntity.ok(response);
	}

	//허브별 배송 담당자 조회
	@GetMapping("/hub/{hubId}")
	public ResponseEntity<List<DeliveryManagerResponseDtoV1>> getDeliveryManagersByHub(
		@PathVariable UUID hubId,
		@RequestHeader(value = "X-User-Id", required = false) String userId
	) {
		validateAuthentication(userId);

		List<DeliveryManagerResponseDtoV1> response = deliveryManagerServiceV1.getDeliveryManagersByHub(hubId);
		return ResponseEntity.ok(response);
	}

	//허브,타입별 배송 담당자 조회
	@GetMapping("/hub/{hubId}/type/{deliveryType}")
	public ResponseEntity<List<DeliveryManagerResponseDtoV1>> getDeliveryManagersByHubAndType(
		@PathVariable UUID hubId,
		@PathVariable DeliveryType deliveryType,
		@RequestHeader(value = "X-User-Id", required = false) String userId
	) {
		validateAuthentication(userId);

		List<DeliveryManagerResponseDtoV1> response = deliveryManagerServiceV1
			.getDeliveryManagersByHubAndType(hubId, deliveryType);
		return ResponseEntity.ok(response);
	}

	//배송 담당자 수정
	@PatchMapping("/{managerId}")
	public ResponseEntity<DeliveryManagerResponseDtoV1> updateDeliveryManager(
		@PathVariable UUID managerId,
		@RequestBody DeliveryManagerUpdateRequestDtoV1 request,
		@RequestHeader(value = "X-User-Name", required = false) String username
	) {
		validateAuthentication(username);

		DeliveryManagerResponseDtoV1 response = deliveryManagerServiceV1
			.updateDeliveryManager(managerId, request, username);
		return ResponseEntity.ok(response);
	}

	//배송 담당자 삭제
	@DeleteMapping("/{managerId}")
	public ResponseEntity<Void> deleteDeliveryManager(
		@PathVariable UUID managerId,
		@RequestHeader(value = "X-User-Name", required = false) String username
	) {
		validateAuthentication(username);

		deliveryManagerServiceV1.deleteDeliveryManager(managerId, username);
		return ResponseEntity.noContent().build();
	}

	private void validateAuthentication(String authInfo) {
		if (authInfo == null || authInfo.isEmpty()) {
			throw new IllegalArgumentException("인증 정보가 없습니다.");
		}
	}
}
