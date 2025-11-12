package com.sparta.cupeed.delivery.domain.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Delivery {

	private UUID id;
	private UUID orderId;
	private UUID receiveCompanyId;
	private UUID startHubId;
	private UUID endHubId;
	private DeliveryStatus status;
	private UUID deliveryManagerId;
	private LocalDateTime createdAt;
	private String createdBy;
	private LocalDateTime updatedAt;
	private String updatedBy;
	private LocalDateTime deletedAt;
	private String deletedBy;

	public static Delivery create(UUID orderId, UUID receiveCompanyId,
		UUID startHubId, UUID endHubId, String createdBy) {
		return Delivery.builder()
			.orderId(orderId)
			.receiveCompanyId(receiveCompanyId)
			.startHubId(startHubId)
			.endHubId(endHubId)
			.status(DeliveryStatus.READY)
			.createdAt(LocalDateTime.now())
			.createdBy(createdBy)
			.build();
	}

	public Delivery updateStatus(DeliveryStatus newStatus, String username) {
		return Delivery.builder()
			.id(this.id)
			.orderId(this.orderId)
			.receiveCompanyId(this.receiveCompanyId)
			.startHubId(this.startHubId)
			.endHubId(this.endHubId)
			.status(newStatus)
			.deliveryManagerId(this.deliveryManagerId)
			.createdAt(this.createdAt)
			.createdBy(this.createdBy)
			.updatedAt(LocalDateTime.now())
			.updatedBy(username)
			.deletedAt(this.deletedAt)
			.deletedBy(this.deletedBy)
			.build();
	}

	public Delivery assignDeliveryManager(UUID managerId, String username) {
		return Delivery.builder()
			.id(this.id)
			.orderId(this.orderId)
			.receiveCompanyId(this.receiveCompanyId)
			.startHubId(this.startHubId)
			.endHubId(this.endHubId)
			.status(this.status)
			.deliveryManagerId(managerId)
			.createdAt(this.createdAt)
			.createdBy(this.createdBy)
			.updatedAt(LocalDateTime.now())
			.updatedBy(username)
			.deletedAt(this.deletedAt)
			.deletedBy(this.deletedBy)
			.build();
	}

	public Delivery softDelete(String username) {
		return Delivery.builder()
			.id(this.id)
			.orderId(this.orderId)
			.receiveCompanyId(this.receiveCompanyId)
			.startHubId(this.startHubId)
			.endHubId(this.endHubId)
			.status(this.status)
			.deliveryManagerId(this.deliveryManagerId)
			.createdAt(this.createdAt)
			.createdBy(this.createdBy)
			.updatedAt(this.updatedAt)
			.updatedBy(this.updatedBy)
			.deletedAt(LocalDateTime.now())
			.deletedBy(username)
			.build();
	}

	public boolean isDeleted() {
		return this.deletedAt != null;
	}
}
