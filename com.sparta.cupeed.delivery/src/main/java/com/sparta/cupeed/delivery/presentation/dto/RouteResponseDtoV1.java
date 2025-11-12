package com.sparta.cupeed.delivery.presentation.dto;

import java.time.Instant;
import java.util.UUID;

import com.sparta.cupeed.delivery.domain.model.DeliveryRoute;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RouteResponseDtoV1 {

	private UUID id;
	private UUID deliveryId;
	private UUID startHubId;
	private UUID endHubId;
	private Double estimatedTotalDistance;
	private String estimatedTotalDuration;
	private Double actualTotalDistance;
	private String actualTotalDuration;
	private String status;
	private String deliveryManagerId;
	private Instant startedAt;
	private Instant completedAt;
	private Instant createdAt;
	private String createdBy;
	private Instant updatedAt;
	private String updatedBy;

	public static RouteResponseDtoV1 from(DeliveryRoute route) {
		return RouteResponseDtoV1.builder()
			.id(route.getId())
			.deliveryId(route.getDeliveryId())
			.startHubId(route.getStartHubId())
			.endHubId(route.getEndHubId())
			.estimatedTotalDistance(route.getEstimatedTotalDistance())
			.estimatedTotalDuration(route.getEstimatedTotalDuration())
			.actualTotalDistance(route.getActualTotalDistance())
			.actualTotalDuration(route.getActualTotalDuration())
			.status(route.getStatus().name())
			.deliveryManagerId(route.getDeliveryManagerId())
			.startedAt(route.getStartedAt())
			.completedAt(route.getCompletedAt())
			.createdAt(route.getCreatedAt())
			.createdBy(route.getCreatedBy())
			.updatedAt(route.getUpdatedAt())
			.updatedBy(route.getUpdatedBy())
			.build();
	}
}