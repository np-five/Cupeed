package com.sparta.cupeed.delivery.presentation.dto;

import java.util.UUID;

import com.sparta.cupeed.delivery.domain.model.Delivery;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryResponseDto {
	private UUID id;
	private UUID orderId;
	private UUID receiveCompanyId;
	private UUID startHubId;
	private UUID endHubId;
	private String status;
	private UUID deliveryManagerId;
	private String createdAt;
	private String createdBy;
	private String updatedAt;
	private String updatedBy;

	public static DeliveryResponseDto from(Delivery delivery) {
		return DeliveryResponseDto.builder()
			.id(delivery.getId())
			.orderId(delivery.getOrderId())
			.receiveCompanyId(delivery.getReceiveCompanyId())
			.startHubId(delivery.getStartHubId())
			.endHubId(delivery.getEndHubId())
			.status(delivery.getStatus().name())
			.deliveryManagerId(delivery.getDeliveryManagerId())
			.createdAt(delivery.getCreatedAt() != null ? delivery.getCreatedAt().toString() : null)
			.createdBy(delivery.getCreatedBy())
			.updatedAt(delivery.getUpdatedAt() != null ? delivery.getUpdatedAt().toString() : null)
			.updatedBy(delivery.getUpdatedBy())
			.build();
	}
}
