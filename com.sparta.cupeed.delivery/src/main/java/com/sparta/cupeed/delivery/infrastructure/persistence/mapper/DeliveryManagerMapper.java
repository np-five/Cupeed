package com.sparta.cupeed.delivery.infrastructure.persistence.mapper;

import org.springframework.stereotype.Component;

import com.sparta.cupeed.delivery.domain.model.DeliveryManager;
import com.sparta.cupeed.delivery.infrastructure.persistence.entity.DeliveryManagerEntity;

@Component
public class DeliveryManagerMapper {

	public DeliveryManager toDomain(DeliveryManagerEntity entity) {
		if (entity == null) {
			return null;
		}

		return DeliveryManager.builder()
			.id(entity.getId())
			.userId(entity.getUserId())
			.hubId(entity.getHubId())
			.deliveryType(DeliveryManager.DeliveryType.valueOf(entity.getDeliveryType().name()))
			.deliverySequence(entity.getDeliverySequence())
			.createdAt(entity.getCreatedAt())
			.createdBy(entity.getCreatedBy())
			.updatedAt(entity.getUpdatedAt())
			.updatedBy(entity.getUpdatedBy())
			.deletedAt(entity.getDeletedAt())
			.deletedBy(entity.getDeletedBy())
			.build();
	}

	public DeliveryManagerEntity toEntity(DeliveryManager domain) {
		if (domain == null) {
			return null;
		}

		return DeliveryManagerEntity.builder()
			.id(domain.getId())
			.userId(domain.getUserId())
			.hubId(domain.getHubId())
			.deliveryType(DeliveryManagerEntity.DeliveryType.valueOf(domain.getDeliveryType().name()))
			.deliverySequence(domain.getDeliverySequence())
			.build();
	}

	public void applyDomain(DeliveryManager domain, DeliveryManagerEntity entity) {
		if (domain == null || entity == null) {
			return;
		}

		entity.changeHub(domain.getHubId());
		entity.changeDeliveryType(DeliveryManagerEntity.DeliveryType.valueOf(domain.getDeliveryType().name()));
		entity.updateDeliverySequence(domain.getDeliverySequence());

		if (domain.getDeletedAt() != null && domain.getDeletedBy() != null) {
			entity.markDeleted(domain.getDeletedBy());
		}
	}
}
