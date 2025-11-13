package com.sparta.cupeed.delivery.domain.model;

import java.time.Instant;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeliveryRoute {

	private UUID id;
	private UUID deliveryId;
	private UUID startHubId;
	private UUID endHubId;
	private Double estimatedTotalDistance;
	private String estimatedTotalDuration;
	private Double actualTotalDistance;
	private String actualTotalDuration;
	private Status status;
	private String deliveryManagerId;
	private Instant startedAt;
	private Instant completedAt;
	private Instant createdAt;
	private String createdBy;
	private Instant updatedAt;
	private String updatedBy;

	public enum Status {
		READY,
		HUB_TRANSIT,
		HUB_ARRIVED
	}

	public void startDelivery(String managerId, Instant startTime) {
		if (this.status != Status.READY) {
			throw new IllegalStateException("대기 중인 배송만 시작할 수 있습니다.");
		}
		this.deliveryManagerId = managerId;
		this.startedAt = startTime;
		this.status = Status.HUB_TRANSIT;
	}

	public void completeDelivery(Double actualDistance, String actualDuration, Instant completeTime) {
		if (this.status != Status.HUB_TRANSIT) {
			throw new IllegalStateException("이동 중인 배송만 완료할 수 있습니다.");
		}
		this.actualTotalDistance = actualDistance;
		this.actualTotalDuration = actualDuration;
		this.completedAt = completeTime;
		this.status = Status.HUB_ARRIVED;
	}

	public void updateStatus(Status newStatus) {
		this.status = newStatus;
	}
}