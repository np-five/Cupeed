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
public class DeliveryServiceV1 {

	private final DeliveryRepository deliveryRepository;

	public DeliveryServiceV1(DeliveryRepository deliveryRepository) {
		this.deliveryRepository = deliveryRepository;
	}

	// 배송 생성
	@Transactional
	public DeliveryResponseDtoV1 createDelivery(DeliveryCreateRequestDtoV1 request, String username) {

		// TODO : 허브 Client 호출
		UUID startHubId = UUID.randomUUID();
		UUID endHubId = UUID.randomUUID();
		// TODO : 배송 담당자 호출
		UUID deliveryManagerId = UUID.randomUUID();

		// request 복사해서 null 값을 임시값으로 채움
		// --> 주문에서는 orderId, receiveCompanyId만 넘겨줄 수 있음
		// 효선님이 만들어 놓으신 toEntity 사용해서 임시 방편으로 일단 이렇게 구현해놨습니다!
		DeliveryCreateRequestDtoV1 temporaryRequest = DeliveryCreateRequestDtoV1.builder()
			.orderId(request.getOrderId())
			.receiveCompanyId(request.getReceiveCompanyId())
			.startHubId(startHubId)
			.endHubId(endHubId)
			.deliveryManagerId(deliveryManagerId)
			.build();

		Delivery delivery = temporaryRequest.toEntity(username);
		Delivery savedDelivery = deliveryRepository.save(delivery);
		return DeliveryResponseDtoV1.from(savedDelivery);
	}

	// 배송 전체 조회
	public DeliveriesResponseDtoV1 getDeliveries(int page, int size) {
		List<Delivery> deliveries = deliveryRepository.findAll(page, size);
		long totalCount = deliveryRepository.countAll();

		List<DeliveryResponseDtoV1> deliveryDtos = deliveries.stream()
			.map(DeliveryResponseDtoV1::from)
			.collect(Collectors.toList());

		return new DeliveriesResponseDtoV1(deliveryDtos, totalCount);
	}

	// 배송 단건 조회
	public DeliveryResponseDtoV1 getDelivery(UUID deliveryId) {
		Delivery delivery = deliveryRepository.findById(deliveryId)
			.orElseThrow(() -> new IllegalArgumentException(
				"배송을 찾을 수 없습니다. ID: " + deliveryId
			));

		return DeliveryResponseDtoV1.from(delivery);
	}

	// 배송 상태 변경
	@Transactional
	public DeliveryResponseDtoV1 updateDeliveryStatus(
		UUID deliveryId,
		DeliveryStatusUpdateRequestDtoV1 request,
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
		return DeliveryResponseDtoV1.from(updatedDelivery);
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