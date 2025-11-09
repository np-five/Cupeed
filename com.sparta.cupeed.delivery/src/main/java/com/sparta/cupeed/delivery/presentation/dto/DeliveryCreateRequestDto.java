package com.sparta.cupeed.delivery.presentation.dto;

import java.util.UUID;

import com.sparta.cupeed.delivery.domain.model.Delivery;

import lombok.Getter;
import lombok.Setter;

// 배송 생성 요청 DTO
@Getter
@Setter
public class DeliveryCreateRequestDto {
	private UUID orderId;
	private UUID receiveCompanyId;
	private UUID startHubId;
	private UUID endHubId;
	private UUID deliveryManagerId;

	public DeliveryCreateRequestDto() {
	}

	public Delivery toEntity(String username) {
		Delivery delivery = new Delivery(
			orderId,
			receiveCompanyId,
			startHubId,
			endHubId,
			username
		);
		if (deliveryManagerId != null) {
			delivery.setDeliveryManagerId(deliveryManagerId);
		}
		return delivery;
	}

}

