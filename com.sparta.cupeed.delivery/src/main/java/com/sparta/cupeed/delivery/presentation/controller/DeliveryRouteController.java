package com.sparta.cupeed.delivery.presentation.controller;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sparta.cupeed.delivery.application.service.DeliveryRouteService;
import com.sparta.cupeed.delivery.domain.model.DeliveryRoute;
import com.sparta.cupeed.delivery.presentation.dto.RouteCompleteRequestDto;
import com.sparta.cupeed.delivery.presentation.dto.RouteCreateRequestDto;
import com.sparta.cupeed.delivery.presentation.dto.RouteStartRequestDto;
import com.sparta.cupeed.delivery.presentation.dto.RouteResponseDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/deliveries/{deliveryId}/routes")
@RequiredArgsConstructor
public class DeliveryRouteController {

	private final DeliveryRouteService deliveryRouteService;

	//배송 경로 생성
	@PostMapping
	public ResponseEntity<RouteResponseDto> createRoute(
		@PathVariable UUID deliveryId,
		@Valid @RequestBody RouteCreateRequestDto requestDto,
		@RequestHeader(value = "X-User-Id", defaultValue = "system") String userId) {

		DeliveryRoute route = deliveryRouteService.createRoute(
			deliveryId,
			requestDto.getStartHubId(),
			requestDto.getEndHubId(),
			requestDto.getEstimatedDistance(),
			requestDto.getEstimatedDuration(),
			userId
		);

		return ResponseEntity.status(HttpStatus.CREATED)
			.body(RouteResponseDto.from(route));
	}

	//배송 경로 시작
	@PatchMapping("/{routeId}/start")
	public ResponseEntity<RouteResponseDto> startRoute(
		@PathVariable UUID deliveryId,
		@PathVariable UUID routeId,
		@Valid @RequestBody RouteStartRequestDto requestDto,
		@RequestHeader(value = "X-User-Id", defaultValue = "system") String userId) {

		DeliveryRoute route = deliveryRouteService.startRoute(
			routeId,
			requestDto.getDeliveryManagerId(),
			userId
		);

		return ResponseEntity.ok(RouteResponseDto.from(route));
	}

	//배송 경로 완료 (배송 담당자가 실제 거리와 소요시간을 직접 입력)
	@PatchMapping("/{routeId}/complete")
	public ResponseEntity<RouteResponseDto> completeRoute(
		@PathVariable UUID deliveryId,
		@PathVariable UUID routeId,
		@Valid @RequestBody RouteCompleteRequestDto requestDto,
		@RequestHeader(value = "X-User-Id", defaultValue = "system") String userId) {

		DeliveryRoute route = deliveryRouteService.completeRoute(
			routeId,
			requestDto.getActualDistance(),
			requestDto.getActualDuration(),
			userId
		);

		return ResponseEntity.ok(RouteResponseDto.from(route));
	}

	//배송 경로 상태 변경
	@PatchMapping("/{routeId}/status")
	public ResponseEntity<RouteResponseDto> updateRouteStatus(
		@PathVariable UUID deliveryId,
		@PathVariable UUID routeId,
		@RequestParam String status,
		@RequestHeader(value = "X-User-Id", defaultValue = "system") String userId) {

		DeliveryRoute.Status newStatus = DeliveryRoute.Status.valueOf(status);

		DeliveryRoute route = deliveryRouteService.updateRouteStatus(
			routeId,
			newStatus,
			userId
		);

		return ResponseEntity.ok(RouteResponseDto.from(route));
	}

	//배송 ID로 경로 목록 조회
	@GetMapping
	public ResponseEntity<List<RouteResponseDto>> getRoutesByDeliveryId(
		@PathVariable UUID deliveryId) {

		List<RouteResponseDto> routes = deliveryRouteService.getRoutesByDeliveryId(deliveryId)
			.stream()
			.map(RouteResponseDto::from)
			.collect(Collectors.toList());

		return ResponseEntity.ok(routes);
	}

	//배송 경로 단건 조회
	@GetMapping("/{routeId}")
	public ResponseEntity<RouteResponseDto> getRoute(
		@PathVariable UUID deliveryId,
		@PathVariable UUID routeId) {

		DeliveryRoute route = deliveryRouteService.getRouteById(routeId);
		return ResponseEntity.ok(RouteResponseDto.from(route));
	}

	//모든 배송 경로 조회
	@GetMapping("/all")
	public ResponseEntity<List<RouteResponseDto>> getAllRoutes() {
		List<RouteResponseDto> routes = deliveryRouteService.getAllRoutes()
			.stream()
			.map(RouteResponseDto::from)
			.collect(Collectors.toList());

		return ResponseEntity.ok(routes);
	}
}