package com.sparta.cupeed.delivery.presentation.dto;

import java.util.UUID;

import com.sparta.cupeed.delivery.domain.model.DeliveryManager;
import com.sparta.cupeed.delivery.domain.model.DeliveryType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryManagerCreateRequestDtoV1 {
	private String userId;
	private UUID hubId;
	private DeliveryType deliveryType;
	private Integer deliverySequence;

	public DeliveryManagerCreateRequestDtoV1() {
		this.deliverySequence = 0;
	}

	public DeliveryManager toEntity(String username) {
		return new DeliveryManager(
			userId,
			hubId,
			deliveryType,
			deliverySequence,
			username
		);
	}
}
