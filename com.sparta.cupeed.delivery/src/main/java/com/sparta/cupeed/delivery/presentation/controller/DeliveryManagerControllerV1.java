package com.sparta.cupeed.delivery.presentation.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sparta.cupeed.delivery.application.service.DeliveryManagerServiceV1;
import com.sparta.cupeed.delivery.domain.model.DeliveryManager;
import com.sparta.cupeed.delivery.domain.model.DeliveryType;
import com.sparta.cupeed.delivery.presentation.dto.DeliveryManagerCreateRequestDtoV1;
import com.sparta.cupeed.delivery.presentation.dto.DeliveryManagerResponseDtoV1;
import com.sparta.cupeed.delivery.presentation.dto.DeliveryManagerUpdateRequestDtoV1;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/delivery-managers")
@RequiredArgsConstructor
public class DeliveryManagerControllerV1 {

	private final DeliveryManagerServiceV1 deliveryManagerServiceV1;

	//배송 담당자 생성
	@PostMapping
	public ResponseEntity<DeliveryManagerResponseDtoV1> createDeliveryManager(
		@Valid @RequestBody DeliveryManagerCreateRequestDtoV1 request,
		@RequestHeader(value = "X-User-Id", defaultValue = "system") String userId) {

		validateAuthentication(userId);

		DeliveryManager manager = deliveryManagerServiceV1.createManager(
			request.getUserId(),
			request.getHubId(),
			request.getDeliveryType(),
			userId
		);

		return ResponseEntity.status(HttpStatus.CREATED)
			.body(DeliveryManagerResponseDtoV1.from(manager));
	}

	//배송 담당자 전체 조회
	@GetMapping
	public ResponseEntity<List<DeliveryManagerResponseDtoV1>> getAllDeliveryManagers(
		@RequestHeader(value = "X-User-Id", required = false) String userId) {

		validateAuthentication(userId);

		List<DeliveryManagerResponseDtoV1> response = deliveryManagerServiceV1.getAllActiveManagers()
			.stream()
			.map(DeliveryManagerResponseDtoV1::from)
			.collect(Collectors.toList());

		return ResponseEntity.ok(response);
	}

	//배송 담당자 단건 조회
	@GetMapping("/{managerId}")
	public ResponseEntity<DeliveryManagerResponseDtoV1> getDeliveryManager(
		@PathVariable UUID managerId,
		@RequestHeader(value = "X-User-Id", required = false) String userId) {

		validateAuthentication(userId);

		DeliveryManager manager = deliveryManagerServiceV1.getManagerById(managerId);
		return ResponseEntity.ok(DeliveryManagerResponseDtoV1.from(manager));
	}

	//사용자 ID로 배송 담당자 조회
	@GetMapping("/user/{userId}")
	public ResponseEntity<DeliveryManagerResponseDtoV1> getDeliveryManagerByUserId(
		@PathVariable String userId,
		@RequestHeader(value = "X-User-Id", required = false) String requestUserId) {

		validateAuthentication(requestUserId);

		DeliveryManager manager = deliveryManagerServiceV1.getManagerByUserId(userId);
		return ResponseEntity.ok(DeliveryManagerResponseDtoV1.from(manager));
	}

	//허브별 배송 담당자 조회
	@GetMapping("/hub/{hubId}")
	public ResponseEntity<List<DeliveryManagerResponseDtoV1>> getDeliveryManagersByHub(
		@PathVariable UUID hubId,
		@RequestHeader(value = "X-User-Id", required = false) String userId) {

		validateAuthentication(userId);

		List<DeliveryManagerResponseDtoV1> response = deliveryManagerServiceV1.getManagersByHubId(hubId)
			.stream()
			.map(DeliveryManagerResponseDtoV1::from)
			.collect(Collectors.toList());

		return ResponseEntity.ok(response);
	}

	//배송 담당자 수정
	@PatchMapping("/{managerId}")
	public ResponseEntity<DeliveryManagerResponseDtoV1> updateDeliveryManager(
		@PathVariable UUID managerId,
		@Valid @RequestBody DeliveryManagerUpdateRequestDtoV1 request,
		@RequestHeader(value = "X-User-Id", defaultValue = "system") String userId) {

		validateAuthentication(userId);

		DeliveryManager manager = deliveryManagerServiceV1.updateManager(
			managerId,
			request.getHubId(),
			request.getDeliveryType(),
			request.getDeliverySequence(),
			userId
		);

		return ResponseEntity.ok(DeliveryManagerResponseDtoV1.from(manager));
	}

	//배송 담당자 삭제 (소프트 딜리트)
	@DeleteMapping("/{managerId}")
	public ResponseEntity<Void> deleteDeliveryManager(
		@PathVariable UUID managerId,
		@RequestHeader(value = "X-User-Id", defaultValue = "system") String userId) {

		validateAuthentication(userId);

		deliveryManagerServiceV1.deleteManager(managerId, userId);
		return ResponseEntity.noContent().build();
	}

	//인증 정보 검증
	private void validateAuthentication(String authInfo) {
		if (authInfo == null || authInfo.isEmpty()) {
			throw new IllegalArgumentException("인증 정보가 없습니다.");
		}
	}
}