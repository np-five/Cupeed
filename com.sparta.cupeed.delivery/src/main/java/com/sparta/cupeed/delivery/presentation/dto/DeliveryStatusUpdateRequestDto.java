package com.sparta.cupeed.delivery.presentation.dto;

import com.sparta.cupeed.delivery.domain.model.DeliveryStatus;

import lombok.Getter;
import lombok.Setter;

// 배송 상태 변경 요청 DTO
@Getter
@Setter
public class DeliveryStatusUpdateRequestDto {
	private DeliveryStatus status;

	public DeliveryStatusUpdateRequestDto() {
	}

	public DeliveryStatus getStatus() {
		return status;
	}

	public void setStatus(DeliveryStatus status) {
		this.status = status;
	}
}
