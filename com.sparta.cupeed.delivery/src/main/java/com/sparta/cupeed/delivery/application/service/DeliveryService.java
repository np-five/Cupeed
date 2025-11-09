package com.sparta.cupeed.delivery.application.service;

import com.sparta.cupeed.delivery.presentation.dto.*;
import com.sparta.cupeed.delivery.domain.model.*;
import com.sparta.cupeed.delivery.domain.repository.DeliveryRepository;
import com.sparta.cupeed.delivery.domain.repository.DeliveryRouteRepository;
import com.sparta.cupeed.delivery.domain.repository.DeliveryManagerRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class DeliveryService {

	private final DeliveryRepository deliveryRepository;
	private final DeliveryRouteRepository deliveryRouteRepository;
	private final DeliveryManagerRepository deliveryManagerRepository;

	public DeliveryService(
		DeliveryRepository deliveryRepository,
		DeliveryRouteRepository deliveryRouteRepository,
		DeliveryManagerRepository deliveryManagerRepository
	) {
		this.deliveryRepository = deliveryRepository;
		this.deliveryRouteRepository = deliveryRouteRepository;
		this.deliveryManagerRepository = deliveryManagerRepository;
	}

	// 배송 생성
	@Transactional
	public DeliveryResponseDto createDelivery(DeliveryCreateRequestDto request, String username) {
		// 배송 담당자 검증 (제공된 경우)
		if (request.getDeliveryManagerId() != null) {
			deliveryManagerRepository.findActiveById(request.getDeliveryManagerId())
				.orElseThrow(() -> new IllegalArgumentException(
					"배송 담당자를 찾을 수 없습니다. ID: " + request.getDeliveryManagerId()
				));
		}

		Delivery delivery = request.toEntity(username);
		Delivery savedDelivery = deliveryRepository.save(delivery);

		return DeliveryResponseDto.from(savedDelivery);
	}

	// 배송 전체 조회
	public DeliveriesResponseDto getDeliveries(Pageable pageable) {
		Page<Delivery> deliveriesPage = deliveryRepository.findAllActive(pageable);
		List<DeliveryResponseDto> deliveries = deliveriesPage.getContent().stream()
			.map(DeliveryResponseDto::from)
			.collect(Collectors.toList());

		return new DeliveriesResponseDto(deliveries, deliveriesPage.getTotalElements());
	}

	// 배송 단건 조회
	public DeliveryResponseDto getDelivery(UUID deliveryId) {
		Delivery delivery = deliveryRepository.findActiveById(deliveryId)
			.orElseThrow(() -> new IllegalArgumentException(
				"배송을 찾을 수 없습니다. ID: " + deliveryId
			));

		return DeliveryResponseDto.from(delivery);
	}

	// 주문 ID로 배송 조회
	public DeliveryResponseDto getDeliveryByOrderId(UUID orderId) {
		Delivery delivery = deliveryRepository.findByOrderId(orderId)
			.orElseThrow(() -> new IllegalArgumentException(
				"해당 주문의 배송을 찾을 수 없습니다. Order ID: " + orderId
			));

		return DeliveryResponseDto.from(delivery);
	}

	// 배송 상태별 조회
	public DeliveriesResponseDto getDeliveriesByStatus(DeliveryStatus status, Pageable pageable) {
		Page<Delivery> deliveriesPage = deliveryRepository.findByStatus(status, pageable);
		List<DeliveryResponseDto> deliveries = deliveriesPage.getContent().stream()
			.map(DeliveryResponseDto::from)
			.collect(Collectors.toList());

		return new DeliveriesResponseDto(deliveries, deliveriesPage.getTotalElements());
	}

	// 배송 수정
	@Transactional
	public DeliveryResponseDto updateDelivery(
		UUID deliveryId,
		DeliveryUpdateRequestDto request,
		String username
	) {
		Delivery delivery = deliveryRepository.findActiveById(deliveryId)
			.orElseThrow(() -> new IllegalArgumentException(
				"배송을 찾을 수 없습니다. ID: " + deliveryId
			));

		// 배송이 이미 시작된 경우 수정 불가
		if (delivery.getStatus() != DeliveryStatus.READY) {
			throw new IllegalStateException("배송이 시작된 후에는 수정할 수 없습니다.");
		}

		// 배송 담당자 변경 시 검증
		if (request.getDeliveryManagerId() != null) {
			deliveryManagerRepository.findActiveById(request.getDeliveryManagerId())
				.orElseThrow(() -> new IllegalArgumentException(
					"배송 담당자를 찾을 수 없습니다. ID: " + request.getDeliveryManagerId()
				));
			delivery.assignDeliveryManager(request.getDeliveryManagerId(), username);
		}

		delivery.setUpdatedBy(username);
		delivery.setUpdatedAt(LocalDateTime.now());

		return DeliveryResponseDto.from(delivery);
	}

	// 배송 상태 변경
	@Transactional
	public DeliveryResponseDto updateDeliveryStatus(
		UUID deliveryId,
		DeliveryStatusUpdateRequestDto request,
		String username
	) {
		Delivery delivery = deliveryRepository.findActiveById(deliveryId)
			.orElseThrow(() -> new IllegalArgumentException(
				"배송을 찾을 수 없습니다. ID: " + deliveryId
			));

		// 상태 변경 유효성 검증
		validateStatusTransition(delivery.getStatus(), request.getStatus());

		delivery.updateStatus(request.getStatus(), username);

		return DeliveryResponseDto.from(delivery);
	}

	// 배송 삭제 (Soft Delete)
	@Transactional
	public void deleteDelivery(UUID deliveryId, String username) {
		Delivery delivery = deliveryRepository.findActiveById(deliveryId)
			.orElseThrow(() -> new IllegalArgumentException(
				"배송을 찾을 수 없습니다. ID: " + deliveryId
			));

		// 배송이 진행 중인 경우 삭제 불가
		if (delivery.getStatus() == DeliveryStatus.HUB_TRANSIT ||
			delivery.getStatus() == DeliveryStatus.COMPANY_TRANSIT) {
			throw new IllegalStateException("진행 중인 배송은 삭제할 수 없습니다.");
		}

		delivery.softDelete(username);
	}

	// 배송 경로 기록 생성
	@Transactional
	public DeliveryRouteResponseDto createDeliveryRoute(
		UUID deliveryId,
		DeliveryRouteCreateRequestDto request,
		String username
	) {
		// 배송 존재 여부 확인
		deliveryRepository.findActiveById(deliveryId)
			.orElseThrow(() -> new IllegalArgumentException(
				"배송을 찾을 수 없습니다. ID: " + deliveryId
			));

		DeliveryRoute route = request.toEntity(username);
		DeliveryRoute savedRoute = deliveryRouteRepository.save(route);

		return DeliveryRouteResponseDto.from(savedRoute);
	}

	// 배송 경로 조회
	public List<DeliveryRouteResponseDto> getDeliveryRoutes(UUID deliveryId) {
		List<DeliveryRoute> routes = deliveryRouteRepository.findByDeliveryId(deliveryId);
		return routes.stream()
			.map(DeliveryRouteResponseDto::from)
			.collect(Collectors.toList());
	}

	// 배송 경로 상태 변경
	@Transactional
	public DeliveryRouteResponseDto updateDeliveryRouteStatus(
		UUID deliveryId,
		UUID routeId,
		DeliveryRouteStatusUpdateRequestDto request,
		String username
	) {
		DeliveryRoute route = deliveryRouteRepository.findActiveById(routeId)
			.orElseThrow(() -> new IllegalArgumentException(
				"배송 경로를 찾을 수 없습니다. ID: " + routeId
			));

		// 배송 ID 일치 여부 확인
		List<DeliveryRoute> routes = deliveryRouteRepository.findByDeliveryId(deliveryId);
		boolean isValidRoute = routes.stream()
			.anyMatch(r -> r.getId().equals(routeId));

		if (!isValidRoute) {
			throw new IllegalArgumentException("해당 배송의 경로가 아닙니다.");
		}

		route.updateStatus(request.getStatus(), username);

		// 실제 거리/시간 업데이트
		if (request.getActualTotalDistance() != null && request.getActualTotalDuration() != null) {
			route.updateActualData(
				request.getActualTotalDistance(),
				request.getActualTotalDuration(),
				username
			);
		}

		return DeliveryRouteResponseDto.from(route);
	}

	// 상태 변경 유효성 검증
	private void validateStatusTransition(DeliveryStatus currentStatus, DeliveryStatus newStatus) {
		Map<DeliveryStatus, List<DeliveryStatus>> validTransitions = Map.of(
			DeliveryStatus.READY, List.of(DeliveryStatus.HUB_TRANSIT),
			DeliveryStatus.HUB_TRANSIT, List.of(DeliveryStatus.HUB_ARRIVED),
			DeliveryStatus.HUB_ARRIVED, List.of(DeliveryStatus.COMPANY_TRANSIT),
			DeliveryStatus.COMPANY_TRANSIT, List.of(DeliveryStatus.DELIVERED)
		);

		List<DeliveryStatus> allowedStatuses = validTransitions.get(currentStatus);
		if (allowedStatuses == null || !allowedStatuses.contains(newStatus)) {
			throw new IllegalStateException(
				"잘못된 상태 변경입니다. 현재: " + currentStatus + ", 변경 시도: " + newStatus
			);
		}
	}
}