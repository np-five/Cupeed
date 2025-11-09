package com.sparta.cupeed.delivery.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// 배송 엔티티
@Entity
@Table(name = "p_delivery")
public class Delivery {

	@Id
	@Column(columnDefinition = "UUID")
	private UUID id;

	@Column(name = "order_id", columnDefinition = "UUID", nullable = false)
	private UUID orderId;

	@Column(name = "receive_company_id", columnDefinition = "UUID", nullable = false)
	private UUID receiveCompanyId;

	@Column(name = "start_hub_id", columnDefinition = "UUID", nullable = false)
	private UUID startHubId;

	@Column(name = "end_hub_id", columnDefinition = "UUID", nullable = false)
	private UUID endHubId;

	@Enumerated(EnumType.STRING)
	@Column(length = 100, nullable = false)
	private DeliveryStatus status;

	@Column(name = "delivery_manager_id", columnDefinition = "UUID")
	private UUID deliveryManagerId;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@Column(name = "created_by", length = 100, nullable = false)
	private String createdBy;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@Column(name = "updated_by", length = 100)
	private String updatedBy;

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;

	@Column(name = "deleted_by", length = 100)
	private String deletedBy;

	// 기본 생성자
	protected Delivery() {
	}

	// 생성자
	public Delivery(UUID orderId, UUID receiveCompanyId, UUID startHubId,
		UUID endHubId, String createdBy) {
		this.id = UUID.randomUUID();
		this.orderId = orderId;
		this.receiveCompanyId = receiveCompanyId;
		this.startHubId = startHubId;
		this.endHubId = endHubId;
		this.status = DeliveryStatus.READY;
		this.createdAt = LocalDateTime.now();
		this.createdBy = createdBy;
	}

	// 비즈니스 메서드
	public void updateStatus(DeliveryStatus newStatus, String username) {
		this.status = newStatus;
		this.updatedAt = LocalDateTime.now();
		this.updatedBy = username;
	}

	public void assignDeliveryManager(UUID managerId, String username) {
		this.deliveryManagerId = managerId;
		this.updatedAt = LocalDateTime.now();
		this.updatedBy = username;
	}

	public void softDelete(String username) {
		this.deletedAt = LocalDateTime.now();
		this.deletedBy = username;
	}

	// Getter/Setter
	public UUID getId() {
		return id;
	}

	public UUID getOrderId() {
		return orderId;
	}

	public UUID getReceiveCompanyId() {
		return receiveCompanyId;
	}

	public UUID getStartHubId() {
		return startHubId;
	}

	public UUID getEndHubId() {
		return endHubId;
	}

	public DeliveryStatus getStatus() {
		return status;
	}

	public UUID getDeliveryManagerId() {
		return deliveryManagerId;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public LocalDateTime getDeletedAt() {
		return deletedAt;
	}

	public String getDeletedBy() {
		return deletedBy;
	}

	public void setDeliveryManagerId(UUID deliveryManagerId) {
		this.deliveryManagerId = deliveryManagerId;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
}
