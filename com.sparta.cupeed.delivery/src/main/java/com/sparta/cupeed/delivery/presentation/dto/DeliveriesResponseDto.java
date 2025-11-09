package com.sparta.cupeed.delivery.presentation.dto;

import java.util.List;

// 배송 전체 조회 응답 DTO
public class DeliveriesResponseDto {
	private List<DeliveryResponseDto> deliveries;
	private Long totalCount;

	public DeliveriesResponseDto() {
	}

	public DeliveriesResponseDto(List<DeliveryResponseDto> deliveries, Long totalCount) {
		this.deliveries = deliveries;
		this.totalCount = totalCount;
	}

	// Getter/Setter
	public List<DeliveryResponseDto> getDeliveries() {
		return deliveries;
	}

	public void setDeliveries(List<DeliveryResponseDto> deliveries) {
		this.deliveries = deliveries;
	}

	public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}
}
