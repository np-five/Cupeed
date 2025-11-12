package com.sparta.cupeed.delivery.presentation.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.sparta.cupeed.delivery.domain.model.DeliveryManager;
import com.sparta.cupeed.delivery.domain.model.DeliveryType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryManagerResponseDtoV1 {
	private UUID id;
	private String userId;
	private UUID hubId;
	private DeliveryType deliveryType;
	private Integer deliverySequence;
	private LocalDateTime createdAt;
	private String createdBy;

	public DeliveryManagerResponseDtoV1() {
	}

	public static DeliveryManagerResponseDtoV1 from(DeliveryManager manager) {
		DeliveryManagerResponseDtoV1 dto = new DeliveryManagerResponseDtoV1();
		dto.id = manager.getId();
		dto.userId = manager.getUserId();
		dto.hubId = manager.getHubId();
		dto.deliveryType = manager.getDeliveryType();
		dto.deliverySequence = manager.getDeliverySequence();
		dto.createdAt = manager.getCreatedAt();
		dto.createdBy = manager.getCreatedBy();
		return dto;
	}
}
