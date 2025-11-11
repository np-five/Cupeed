package com.sparta.cupeed.delivery.application.service;

import com.sparta.cupeed.delivery.presentation.dto.*;
import com.sparta.cupeed.delivery.domain.model.*;
import com.sparta.cupeed.delivery.domain.repository.DeliveryRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class DeliveryService {

	private final DeliveryRepository deliveryRepository;

	public DeliveryService(DeliveryRepository deliveryRepository) {
		this.deliveryRepository = deliveryRepository;
	}

	// 배송 생성
	@Transactional
	public DeliveryResponseDto createDelivery(DeliveryCreateRequestDto request, String username) {
		Delivery delivery = request.toEntity(username);
		Delivery savedDelivery = deliveryRepository.save(delivery);
		return DeliveryResponseDto.from(savedDelivery);
	}

	// 배송 전체 조회
	public DeliveriesResponseDto getDeliveries(int page, int size) {
		List<Delivery> deliveries = deliveryRepository.findAll(page, size);
		long totalCount = deliveryRepository.countAll();

		List<DeliveryResponseDto> deliveryDtos = deliveries.stream()
			.map(DeliveryResponseDto::from)
			.collect(Collectors.toList());

		return new DeliveriesResponseDto(deliveryDtos, totalCount);
	}

	// 배송 단건 조회
	public DeliveryResponseDto getDelivery(UUID deliveryId) {
		Delivery delivery = deliveryRepository.findById(deliveryId)
			.orElseThrow(() -> new IllegalArgumentException(
				"배송을 찾을 수 없습니다. ID: " + deliveryId
			));

		return DeliveryResponseDto.from(delivery);
	}

	// 배송 상태 변경
	@Transactional
	public DeliveryResponseDto updateDeliveryStatus(
		UUID deliveryId,
		DeliveryStatusUpdateRequestDto request,
		String username
	) {
		Delivery delivery = deliveryRepository.findById(deliveryId)
			.orElseThrow(() -> new IllegalArgumentException(
				"배송을 찾을 수 없습니다. ID: " + deliveryId
			));

		// 상태 변경 유효성 검증
		validateStatusTransition(delivery.getStatus(), request.getStatus());
		delivery.updateStatus(request.getStatus(), username);

		Delivery updatedDelivery = deliveryRepository.save(delivery);
		return DeliveryResponseDto.from(updatedDelivery);
	}

	// 배송 삭제 (Soft Delete)
	@Transactional
	public void deleteDelivery(UUID deliveryId, String username) {
		Delivery delivery = deliveryRepository.findById(deliveryId)
			.orElseThrow(() -> new IllegalArgumentException(
				"배송을 찾을 수 없습니다. ID: " + deliveryId
			));

		delivery.softDelete(username);
		deliveryRepository.save(delivery);
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