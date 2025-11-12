package com.sparta.cupeed.delivery.domain.model;

import java.time.Instant;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DeliveryManager {

	private UUID id;
	private String userId;
	private UUID hubId;
	private DeliveryType deliveryType;
	private Integer deliverySequence;
	private Instant createdAt;
	private String createdBy;
	private Instant updatedAt;
	private String updatedBy;
	private Instant deletedAt;
	private String deletedBy;

	public enum DeliveryType {
		HUB,        // 허브 간 배송
		COMPANY     // 업체 배송
	}

	public void updateDeliverySequence(Integer newSequence) {
		if (newSequence == null || newSequence < 0) {
			throw new IllegalArgumentException("배송 순번은 0 이상이어야 합니다.");
		}
		this.deliverySequence = newSequence;
	}

	public void changeHub(UUID newHubId) {
		if (newHubId == null) {
			throw new IllegalArgumentException("허브 ID는 필수입니다.");
		}
		this.hubId = newHubId;
	}

	public void changeDeliveryType(DeliveryType newType) {
		if (newType == null) {
			throw new IllegalArgumentException("배송 유형은 필수입니다.");
		}
		this.deliveryType = newType;
	}

	public void markDeleted(String deletedBy) {
		this.deletedAt = Instant.now();
		this.deletedBy = deletedBy;
	}

	public boolean isHubDeliveryManager() {
		return DeliveryType.HUB.equals(this.deliveryType);
	}

	public boolean isCompanyDeliveryManager() {
		return DeliveryType.COMPANY.equals(this.deliveryType);
	}
}