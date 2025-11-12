package com.sparta.cupeed.delivery.infrastructure.persistence.entity;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_delivery_routes")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryRouteEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(name = "delivery_id", nullable = false)
	private UUID deliveryId;

	@Column(name = "start_hub_id", nullable = false)
	private UUID startHubId;

	@Column(name = "end_hub_id", nullable = false)
	private UUID endHubId;

	@Column(name = "estimated_total_distance")
	private Double estimatedTotalDistance;

	@Column(name = "estimated_total_duration", length = 100)
	private String estimatedTotalDuration;

	@Column(name = "actual_total_distance")
	private Double actualTotalDistance;

	@Column(name = "actual_total_duration", length = 100)
	private String actualTotalDuration;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", length = 100, nullable = false)
	private Status status;

	@Column(name = "delivery_manager_id", length = 100)
	private String deliveryManagerId;

	@Column(name = "started_at")
	private Instant startedAt;

	@Column(name = "completed_at")
	private Instant completedAt;

	public enum Status {
		READY,
		HUB_TRANSIT,
		HUB_ARRIVED
	}

	@PrePersist
	protected void onCreate() {
		if (this.status == null) {
			this.status = Status.READY;
		}
	}

	public void startDelivery(String managerId, Instant startTime) {
		this.deliveryManagerId = managerId;
		this.startedAt = startTime;
		this.status = Status.HUB_TRANSIT;
	}

	public void completeDelivery(Double actualDistance, String actualDuration, Instant completeTime) {
		this.actualTotalDistance = actualDistance;
		this.actualTotalDuration = actualDuration;
		this.completedAt = completeTime;
		this.status = Status.HUB_ARRIVED;
	}

	public void updateStatus(Status newStatus) {
		this.status = newStatus;
	}
}