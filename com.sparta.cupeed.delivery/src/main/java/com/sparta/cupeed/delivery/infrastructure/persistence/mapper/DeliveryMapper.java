package com.sparta.cupeed.delivery.infrastructure.persistence.mapper;

import com.sparta.cupeed.delivery.domain.model.Delivery;
import com.sparta.cupeed.delivery.domain.model.DeliveryStatus;
import com.sparta.cupeed.delivery.infrastructure.persistence.entity.DeliveryJpaEntity;

public class DeliveryMapper {

	// domain → JPA entity (builder 사용)
	public static DeliveryJpaEntity toJpaEntity(Delivery domain) {
		return DeliveryJpaEntity.builder()
			.id(domain.getId())
			.orderId(domain.getOrderId())
			.receiveCompanyId(domain.getReceiveCompanyId())
			.startHubId(domain.getStartHubId())
			.endHubId(domain.getEndHubId())
			.status(domain.getStatus().name())
			.deliveryManagerId(domain.getDeliveryManagerId())
			.createdAt(domain.getCreatedAt())
			.createdBy(domain.getCreatedBy())
			.updatedAt(domain.getUpdatedAt())
			.updatedBy(domain.getUpdatedBy())
			.deletedAt(domain.getDeletedAt())
			.deletedBy(domain.getDeletedBy())
			.build();
	}

	// JPA entity → domain (builder 사용)
	public static Delivery toDomain(DeliveryJpaEntity entity) {
		return Delivery.builder()
			.id(entity.getId())
			.orderId(entity.getOrderId())
			.receiveCompanyId(entity.getReceiveCompanyId())
			.startHubId(entity.getStartHubId())
			.endHubId(entity.getEndHubId())
			.status(DeliveryStatus.valueOf(entity.getStatus()))
			.deliveryManagerId(entity.getDeliveryManagerId())
			.createdAt(entity.getCreatedAt())
			.createdBy(entity.getCreatedBy())
			.updatedAt(entity.getUpdatedAt())
			.updatedBy(entity.getUpdatedBy())
			.deletedAt(entity.getDeletedAt())
			.deletedBy(entity.getDeletedBy())
			.build();
	}
}