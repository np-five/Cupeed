package com.sparta.cupeed.delivery.presentation.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

// 배송 전체 조회 응답 DTO
@Getter
@Setter
public class DeliveriesResponseDto {
	private List<DeliveryResponseDto> deliveries;
	private Long totalCount;

	public DeliveriesResponseDto() {
	}

	public DeliveriesResponseDto(List<DeliveryResponseDto> deliveries, Long totalCount) {
		this.deliveries = deliveries;
		this.totalCount = totalCount;
	}
}
