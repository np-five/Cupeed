package com.sparta.cupeed.delivery.presentation.dto;

import java.time.Instant;
import java.util.UUID;

import com.sparta.cupeed.delivery.domain.model.DeliveryRoute;

import lombok.Getter;
import lombok.Setter;

// 배송 경로 기록 응답 DTO
@Getter
@Setter
public class DeliveryRouteResponseDtoV1 {
	private UUID id;
	private UUID deliveryId;  // orderId → deliveryId
	private UUID startHubId;
	private UUID endHubId;
	private Double estimatedTotalDistance;
	private String estimatedTotalDuration;
	private Double actualTotalDistance;
	private String actualTotalDuration;
	private DeliveryRoute.Status status;  // RouteStatus → DeliveryRoute.Status
	private String deliveryManagerId;
	private Instant startedAt;  // LocalDateTime → Instant
	private Instant completedAt;  // LocalDateTime → Instant
	private Instant createdAt;  // LocalDateTime → Instant
	private String createdBy;
	private Instant updatedAt;  // LocalDateTime → Instant
	private String updatedBy;

	public DeliveryRouteResponseDtoV1() {
	}

	public static DeliveryRouteResponseDtoV1 from(DeliveryRoute route) {
		DeliveryRouteResponseDtoV1 dto = new DeliveryRouteResponseDtoV1();
		dto.id = route.getId();
		dto.deliveryId = route.getDeliveryId();  // orderId → deliveryId
		dto.startHubId = route.getStartHubId();
		dto.endHubId = route.getEndHubId();
		dto.estimatedTotalDistance = route.getEstimatedTotalDistance();
		dto.estimatedTotalDuration = route.getEstimatedTotalDuration();
		dto.actualTotalDistance = route.getActualTotalDistance();
		dto.actualTotalDuration = route.getActualTotalDuration();
		dto.status = route.getStatus();
		dto.deliveryManagerId = route.getDeliveryManagerId();
		dto.startedAt = route.getStartedAt();
		dto.completedAt = route.getCompletedAt();
		dto.createdAt = route.getCreatedAt();
		dto.createdBy = route.getCreatedBy();
		dto.updatedAt = route.getUpdatedAt();
		dto.updatedBy = route.getUpdatedBy();
		return dto;
	}
}