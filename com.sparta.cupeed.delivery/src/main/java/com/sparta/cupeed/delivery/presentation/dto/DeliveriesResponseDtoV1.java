package com.sparta.cupeed.delivery.presentation.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

// 배송 전체 조회 응답 DTO
@Getter
@Setter
public class DeliveriesResponseDtoV1 {
	private List<DeliveryResponseDtoV1> deliveries;
	private Long totalCount;

	public DeliveriesResponseDtoV1() {
	}

	public DeliveriesResponseDtoV1(List<DeliveryResponseDtoV1> deliveries, Long totalCount) {
		this.deliveries = deliveries;
		this.totalCount = totalCount;
	}
}
