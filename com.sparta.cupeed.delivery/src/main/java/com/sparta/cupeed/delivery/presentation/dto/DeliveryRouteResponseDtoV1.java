package com.sparta.cupeed.delivery.presentation.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.sparta.cupeed.delivery.domain.model.DeliveryRoute;
import com.sparta.cupeed.delivery.domain.model.RouteStatus;

import lombok.Getter;
import lombok.Setter;

// 배송 경로 기록 응답 DTO
@Getter
@Setter
public class DeliveryRouteResponseDtoV1 {
	private UUID id;
	private UUID orderId;
	private Integer routeSequence;
	private UUID startHubId;
	private UUID endHubId;
	private Double estimatedTotalDistance;
	private String estimatedTotalDuration;
	private Double actualTotalDistance;
	private String actualTotalDuration;
	private RouteStatus status;
	private String deliveryManagerId;
	private LocalDateTime startedAt;
	private LocalDateTime completedAt;
	private LocalDateTime createdAt;
	private String createdBy;
	private LocalDateTime updatedAt;
	private String updatedBy;

	public DeliveryRouteResponseDtoV1() {
	}

	public static DeliveryRouteResponseDtoV1 from(DeliveryRoute route) {
		DeliveryRouteResponseDtoV1 dto = new DeliveryRouteResponseDtoV1();
		dto.id = route.getId();
		dto.orderId = route.getOrderId();
		dto.routeSequence = route.getRouteSequence();
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
