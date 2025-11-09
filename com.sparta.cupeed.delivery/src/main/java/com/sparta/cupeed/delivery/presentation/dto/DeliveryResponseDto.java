package com.sparta.cupeed.delivery.presentation.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.sparta.cupeed.delivery.domain.model.Delivery;
import com.sparta.cupeed.delivery.domain.model.DeliveryStatus;

import lombok.Getter;
import lombok.Setter;

// 배송 응답 DTO
@Getter
@Setter
public class DeliveryResponseDto {
	private UUID id;
	private UUID orderId;
	private UUID receiveCompanyId;
	private UUID startHubId;
	private UUID endHubId;
	private DeliveryStatus status;
	private UUID deliveryManagerId;
	private LocalDateTime createdAt;
	private String createdBy;
	private LocalDateTime updatedAt;
	private String updatedBy;

	public DeliveryResponseDto() {
	}

	public static DeliveryResponseDto from(Delivery delivery) {
		DeliveryResponseDto dto = new DeliveryResponseDto();
		dto.id = delivery.getId();
		dto.orderId = delivery.getOrderId();
		dto.receiveCompanyId = delivery.getReceiveCompanyId();
		dto.startHubId = delivery.getStartHubId();
		dto.endHubId = delivery.getEndHubId();
		dto.status = delivery.getStatus();
		dto.deliveryManagerId = delivery.getDeliveryManagerId();
		dto.createdAt = delivery.getCreatedAt();
		dto.createdBy = delivery.getCreatedBy();
		dto.updatedAt = delivery.getUpdatedAt();
		dto.updatedBy = delivery.getUpdatedBy();
		return dto;
	}
	
}
