package com.sparta.cupeed.delivery.presentation.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sparta.cupeed.delivery.application.service.DeliveryRouteServiceV1;
import com.sparta.cupeed.delivery.domain.model.DeliveryRoute;
import com.sparta.cupeed.delivery.presentation.dto.RouteCompleteRequestDtoV1;
import com.sparta.cupeed.delivery.presentation.dto.RouteCreateRequestDtoV1;
import com.sparta.cupeed.delivery.presentation.dto.RouteStartRequestDtoV1;
import com.sparta.cupeed.delivery.presentation.dto.RouteResponseDtoV1;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/deliveries/{deliveryId}/routes")
@RequiredArgsConstructor
public class DeliveryRouteControllerV1 {

	private final DeliveryRouteServiceV1 deliveryRouteServiceV1;

	//배송 경로 생성
	@PostMapping
	public ResponseEntity<RouteResponseDtoV1> createRoute(
		@PathVariable UUID deliveryId,
		@Valid @RequestBody RouteCreateRequestDtoV1 requestDto,
		@RequestHeader(value = "X-User-Id", defaultValue = "system") String userId) {

		DeliveryRoute route = deliveryRouteServiceV1.createRoute(
			deliveryId,
			requestDto.getStartHubId(),
			requestDto.getEndHubId(),
			requestDto.getEstimatedDistance(),
			requestDto.getEstimatedDuration(),
			userId
		);

		return ResponseEntity.status(HttpStatus.CREATED)
			.body(RouteResponseDtoV1.from(route));
	}

	//배송 경로 시작
	@PatchMapping("/{routeId}/start")
	public ResponseEntity<RouteResponseDtoV1> startRoute(
		@PathVariable UUID deliveryId,
		@PathVariable UUID routeId,
		@Valid @RequestBody RouteStartRequestDtoV1 requestDto,
		@RequestHeader(value = "X-User-Id", defaultValue = "system") String userId) {

		DeliveryRoute route = deliveryRouteServiceV1.startRoute(
			routeId,
			requestDto.getDeliveryManagerId(),
			userId
		);

		return ResponseEntity.ok(RouteResponseDtoV1.from(route));
	}

	//배송 경로 완료 (배송 담당자가 실제 거리와 소요시간을 직접 입력)
	@PatchMapping("/{routeId}/complete")
	public ResponseEntity<RouteResponseDtoV1> completeRoute(
		@PathVariable UUID deliveryId,
		@PathVariable UUID routeId,
		@Valid @RequestBody RouteCompleteRequestDtoV1 requestDto,
		@RequestHeader(value = "X-User-Id", defaultValue = "system") String userId) {

		DeliveryRoute route = deliveryRouteServiceV1.completeRoute(
			routeId,
			requestDto.getActualDistance(),
			requestDto.getActualDuration(),
			userId
		);

		return ResponseEntity.ok(RouteResponseDtoV1.from(route));
	}

	//배송 경로 상태 변경
	@PatchMapping("/{routeId}/status")
	public ResponseEntity<RouteResponseDtoV1> updateRouteStatus(
		@PathVariable UUID deliveryId,
		@PathVariable UUID routeId,
		@RequestParam String status,
		@RequestHeader(value = "X-User-Id", defaultValue = "system") String userId) {

		DeliveryRoute.Status newStatus = DeliveryRoute.Status.valueOf(status);

		DeliveryRoute route = deliveryRouteServiceV1.updateRouteStatus(
			routeId,
			newStatus,
			userId
		);

		return ResponseEntity.ok(RouteResponseDtoV1.from(route));
	}

	//배송 ID로 경로 목록 조회
	@GetMapping
	public ResponseEntity<List<RouteResponseDtoV1>> getRoutesByDeliveryId(
		@PathVariable UUID deliveryId) {

		List<RouteResponseDtoV1> routes = deliveryRouteServiceV1.getRoutesByDeliveryId(deliveryId)
			.stream()
			.map(RouteResponseDtoV1::from)
			.collect(Collectors.toList());

		return ResponseEntity.ok(routes);
	}

	//배송 경로 단건 조회
	@GetMapping("/{routeId}")
	public ResponseEntity<RouteResponseDtoV1> getRoute(
		@PathVariable UUID deliveryId,
		@PathVariable UUID routeId) {

		DeliveryRoute route = deliveryRouteServiceV1.getRouteById(routeId);
		return ResponseEntity.ok(RouteResponseDtoV1.from(route));
	}

	//모든 배송 경로 조회
	@GetMapping("/all")
	public ResponseEntity<List<RouteResponseDtoV1>> getAllRoutes() {
		List<RouteResponseDtoV1> routes = deliveryRouteServiceV1.getAllRoutes()
			.stream()
			.map(RouteResponseDtoV1::from)
			.collect(Collectors.toList());

		return ResponseEntity.ok(routes);
	}
}