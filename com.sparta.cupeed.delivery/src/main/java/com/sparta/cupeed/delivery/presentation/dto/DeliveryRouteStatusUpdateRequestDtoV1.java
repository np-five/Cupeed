package com.sparta.cupeed.delivery.presentation.dto;

import com.sparta.cupeed.delivery.domain.model.RouteStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryRouteStatusUpdateRequestDtoV1 {
	private RouteStatus status;
	private Double actualTotalDistance;
	private String actualTotalDuration;

	public DeliveryRouteStatusUpdateRequestDtoV1() {
	}
}
