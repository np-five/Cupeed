package com.sparta.cupeed.delivery.presentation.dto;

import java.util.UUID;

// 배송 수정 요청 DTO
public class DeliveryUpdateRequestDto {
	private UUID receiveCompanyId;
	private UUID startHubId;
	private UUID endHubId;
	private UUID deliveryManagerId;

	public DeliveryUpdateRequestDto() {
	}

	// Getter/Setter
	public UUID getReceiveCompanyId() {
		return receiveCompanyId;
	}

	public void setReceiveCompanyId(UUID receiveCompanyId) {
		this.receiveCompanyId = receiveCompanyId;
	}

	public UUID getStartHubId() {
		return startHubId;
	}

	public void setStartHubId(UUID startHubId) {
		this.startHubId = startHubId;
	}

	public UUID getEndHubId() {
		return endHubId;
	}

	public void setEndHubId(UUID endHubId) {
		this.endHubId = endHubId;
	}

	public UUID getDeliveryManagerId() {
		return deliveryManagerId;
	}

	public void setDeliveryManagerId(UUID deliveryManagerId) {
		this.deliveryManagerId = deliveryManagerId;
	}
}
