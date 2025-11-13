package com.sparta.cupeed.delivery.presentation.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryManagersResponseDtoV1 {
	private List<DeliveryManagerResponseDtoV1> managers;
	private Long totalCount;

	public DeliveryManagersResponseDtoV1() {
	}

	public DeliveryManagersResponseDtoV1(List<DeliveryManagerResponseDtoV1> managers, Long totalCount) {
		this.managers = managers;
		this.totalCount = totalCount;
	}

}