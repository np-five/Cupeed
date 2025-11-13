package com.sparta.cupeed.delivery.application.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.cupeed.delivery.domain.model.DeliveryRoute;
import com.sparta.cupeed.delivery.domain.repository.DeliveryRouteRepository;
import com.sparta.cupeed.delivery.infrastructure.ai.client.AiClientV1;
import com.sparta.cupeed.delivery.infrastructure.ai.dto.GeminiSendRequestDtoV1;
import com.sparta.cupeed.delivery.presentation.dto.DeliveryCreateRequestDtoV1;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeliveryRouteServiceV1 {

	private final DeliveryRouteRepository deliveryRouteRepository;
	private final AiClientV1 aiClient;

	//배송 경로 생성
	@Transactional
	public DeliveryRoute createRoute(UUID deliveryId, UUID startHubId, UUID endHubId,
		Double estimatedDistance, String estimatedDuration,
		String createdBy) {
		DeliveryRoute route = DeliveryRoute.builder()
			.deliveryId(deliveryId)
			.startHubId(startHubId)
			.endHubId(endHubId)
			.estimatedTotalDistance(estimatedDistance)
			.estimatedTotalDuration(estimatedDuration)
			.status(DeliveryRoute.Status.READY)
			.createdBy(createdBy)
			.createdAt(Instant.now())
			.build();

		return deliveryRouteRepository.save(route);
	}

	//배송 경로 시작
	@Transactional
	public DeliveryRoute startRoute(UUID routeId, String managerId, String updatedBy) {
		DeliveryRoute route = deliveryRouteRepository.findById(routeId)
			.orElseThrow(() -> new IllegalArgumentException("배송 경로를 찾을 수 없습니다: " + routeId));

		route.startDelivery(managerId, Instant.now());
		DeliveryRoute savedRoute = deliveryRouteRepository.save(route);

		//TODO: AI에게 배송 정보 전송 - GeminiSendRequestDtoV1에 있는 정보들을 AiClent에게 넘겨줘야 함
		// {
		//   "orderId": "5a4f0e4c-9e6a-4f8c-8f26-31e6b1e7f0f5",
		//   "orderNumber": "ORD-20251208-001",
		//   "recieveCompanyName": "해산물월드",
		//   "orderDate": "2025-12-08T10:00:00Z",
		//   "products": [
		//     {
		//       "productName": "마른 오징어",
		//       "quantity": 600
		//     },
		//     {
		//       "productName": "건어물",
		//       "quantity": 1000
		//     }
		//   ],
		//   "customerRequest": "12월 12일 오후 3시까지 도착하도록 해주세요.",
		//   "deliveryManagerName": "고길동",
		//   "startHubName": "경기 북부 센터",
		//   "endHubName": "부산시 사하구 낙동대로 1번길 1 해산물월드"
		// }
		// List<GeminiSendRequestDtoV1.ProductInfo> productList = List.of(
		// 	GeminiSendRequestDtoV1.ProductInfo.builder()
		// 		.productName()
		// 		.quantity()
		// 		.build()
		// );
		// GeminiSendRequestDtoV1 aiRequestDto = GeminiSendRequestDtoV1.builder()
		// 		.orderId()
		// 		.orderNumber()
		// 		.recieveCompanyName()
		// 		.orderDate()
		// 	    .products(productList)
		// 	    .customerRequest()
		// 	    .deliveryManagerName()
		// 	    .startHubName()
		// 	    .endHubName().build();
		// aiClient.createAiText(aiRequestDto);

		return savedRoute;
	}

	//배송 경로 완료 (배송 담당자가 실제 거리와 소요시간을 직접 입력)
	@Transactional
	public DeliveryRoute completeRoute(UUID routeId, Double actualDistance,
		String actualDuration, String updatedBy) {
		DeliveryRoute route = deliveryRouteRepository.findById(routeId)
			.orElseThrow(() -> new IllegalArgumentException("배송 경로를 찾을 수 없습니다: " + routeId));

		// 배송 시작 여부 검증
		if (route.getStartedAt() == null) {
			throw new IllegalStateException("배송이 시작되지 않은 경로는 완료할 수 없습니다");
		}

		// 배송 상태 검증
		if (route.getStatus() != DeliveryRoute.Status.HUB_TRANSIT) {
			throw new IllegalStateException("배송 중인 경로만 완료할 수 있습니다. 현재 상태: " + route.getStatus());
		}

		// 입력값 검증
		validateDistanceAndDuration(actualDistance, actualDuration);

		route.completeDelivery(actualDistance, actualDuration, Instant.now());
		return deliveryRouteRepository.save(route);
	}

	//거리와 소요시간 입력값 검증
	private void validateDistanceAndDuration(Double distance, String duration) {
		if (distance == null || distance <= 0) {
			throw new IllegalArgumentException("실제 이동 거리는 0보다 커야 합니다");
		}

		if (duration == null || duration.isBlank()) {
			throw new IllegalArgumentException("실제 소요시간을 입력해주세요");
		}
	}

	//배송 경로 상태 변경
	@Transactional
	public DeliveryRoute updateRouteStatus(UUID routeId, DeliveryRoute.Status newStatus,
		String updatedBy) {
		DeliveryRoute route = deliveryRouteRepository.findById(routeId)
			.orElseThrow(() -> new IllegalArgumentException("배송 경로를 찾을 수 없습니다: " + routeId));

		route.updateStatus(newStatus);
		return deliveryRouteRepository.save(route);
	}

	//배송 ID로 경로 목록 조회
	public List<DeliveryRoute> getRoutesByDeliveryId(UUID deliveryId) {
		return deliveryRouteRepository.findByDeliveryId(deliveryId);
	}

	//배송 경로 단건 조회
	public DeliveryRoute getRouteById(UUID routeId) {
		return deliveryRouteRepository.findById(routeId)
			.orElseThrow(() -> new IllegalArgumentException("배송 경로를 찾을 수 없습니다: " + routeId));
	}

	//모든 배송 경로 조회
	public List<DeliveryRoute> getAllRoutes() {
		return deliveryRouteRepository.findAll();
	}
}