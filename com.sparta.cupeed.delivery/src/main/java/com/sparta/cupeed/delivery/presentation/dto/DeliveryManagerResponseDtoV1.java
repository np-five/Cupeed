package com.sparta.cupeed.delivery.presentation.dto;

import java.time.Instant;
import java.util.UUID;

import com.sparta.cupeed.delivery.domain.model.DeliveryManager;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeliveryManagerResponseDtoV1 {

	private UUID id;
	private String userId;
	private UUID hubId;
	private String deliveryType;
	private Integer deliverySequence;
	private Instant createdAt;
	private String createdBy;
	private Instant updatedAt;
	private String updatedBy;
	private Instant deletedAt;
	private String deletedBy;

	public static DeliveryManagerResponseDtoV1 from(DeliveryManager manager) {
		return DeliveryManagerResponseDtoV1.builder()
			.id(manager.getId())
			.userId(manager.getUserId())
			.hubId(manager.getHubId())
			.deliveryType(manager.getDeliveryType().name())
			.deliverySequence(manager.getDeliverySequence())
			.createdAt(manager.getCreatedAt())
			.createdBy(manager.getCreatedBy())
			.updatedAt(manager.getUpdatedAt())
			.updatedBy(manager.getUpdatedBy())
			.deletedAt(manager.getDeletedAt())
			.deletedBy(manager.getDeletedBy())
			.build();
	}
}