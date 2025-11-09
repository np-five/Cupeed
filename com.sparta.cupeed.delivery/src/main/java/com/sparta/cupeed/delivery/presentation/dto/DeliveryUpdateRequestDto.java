package com.sparta.cupeed.delivery.presentation.dto;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

// 배송 수정 요청 DTO
@Getter
@Setter
public class DeliveryUpdateRequestDto {
	private UUID receiveCompanyId;
	private UUID startHubId;
	private UUID endHubId;
	private UUID deliveryManagerId;

	public DeliveryUpdateRequestDto() {
	}
}
