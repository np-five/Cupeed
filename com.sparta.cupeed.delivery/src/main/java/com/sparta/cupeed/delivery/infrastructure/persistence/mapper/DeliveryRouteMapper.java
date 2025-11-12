package com.sparta.cupeed.delivery.infrastructure.persistence.mapper;

import org.springframework.stereotype.Component;

import com.sparta.cupeed.delivery.domain.model.DeliveryRoute;
import com.sparta.cupeed.delivery.infrastructure.persistence.entity.DeliveryRouteEntity;

@Component
public class DeliveryRouteMapper {

	//entity -> domain
	public DeliveryRoute toDomain(DeliveryRouteEntity entity) {
		if (entity == null) {
			return null;
		}

		return DeliveryRoute.builder()
			.id(entity.getId())
			.deliveryId(entity.getDeliveryId())
			.startHubId(entity.getStartHubId())
			.endHubId(entity.getEndHubId())
			.estimatedTotalDistance(entity.getEstimatedTotalDistance())
			.estimatedTotalDuration(entity.getEstimatedTotalDuration())
			.actualTotalDistance(entity.getActualTotalDistance())
			.actualTotalDuration(entity.getActualTotalDuration())
			.status(DeliveryRoute.Status.valueOf(entity.getStatus().name()))
			.deliveryManagerId(entity.getDeliveryManagerId())
			.startedAt(entity.getStartedAt())
			.completedAt(entity.getCompletedAt())
			// Audit 필드는 BaseEntity에서 자동 관리
			.createdAt(entity.getCreatedAt())
			.createdBy(entity.getCreatedBy())
			.updatedAt(entity.getUpdatedAt())
			.updatedBy(entity.getUpdatedBy())
			.build();
	}

	//Domain -> Entity 변환
	public DeliveryRouteEntity toEntity(DeliveryRoute domain) {
		if (domain == null) {
			return null;
		}

		return DeliveryRouteEntity.builder()
			.id(domain.getId())
			.deliveryId(domain.getDeliveryId())
			.startHubId(domain.getStartHubId())
			.endHubId(domain.getEndHubId())
			.estimatedTotalDistance(domain.getEstimatedTotalDistance())
			.estimatedTotalDuration(domain.getEstimatedTotalDuration())
			.actualTotalDistance(domain.getActualTotalDistance())
			.actualTotalDuration(domain.getActualTotalDuration())
			.status(DeliveryRouteEntity.Status.valueOf(domain.getStatus().name()))
			.deliveryManagerId(domain.getDeliveryManagerId())
			.startedAt(domain.getStartedAt())
			.completedAt(domain.getCompletedAt())
			// Audit 필드는 JPA Auditing에서 자동 처리
			.build();
	}

	/**
	 * 도메인의 변경사항을 Entity에 반영
	 */
	public void applyDomain(DeliveryRoute domain, DeliveryRouteEntity entity) {
		if (domain == null || entity == null) {
			return;
		}

		entity.updateStatus(DeliveryRouteEntity.Status.valueOf(domain.getStatus().name()));
	}
}
