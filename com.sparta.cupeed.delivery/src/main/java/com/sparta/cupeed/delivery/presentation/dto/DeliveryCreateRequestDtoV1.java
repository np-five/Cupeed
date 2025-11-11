package com.sparta.cupeed.delivery.presentation.dto;

import com.sparta.cupeed.delivery.domain.model.Delivery;

import lombok.*;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryCreateRequestDtoV1 {
	private UUID orderId;
	private UUID receiveCompanyId;
	private UUID startHubId;
	private UUID endHubId;
	private UUID deliveryManagerId;

	public Delivery toEntity(String username) {
		return Delivery.create(orderId, receiveCompanyId, startHubId, endHubId, username);
	}
}

