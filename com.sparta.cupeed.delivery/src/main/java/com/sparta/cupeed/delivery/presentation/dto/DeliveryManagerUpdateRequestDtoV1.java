package com.sparta.cupeed.delivery.presentation.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryManagerUpdateRequestDtoV1 {
	private Integer deliverySequence;

	public DeliveryManagerUpdateRequestDtoV1() {
	}

	public Integer getDeliverySequence() {
		return deliverySequence;
	}

	public void setDeliverySequence(Integer deliverySequence) {
		this.deliverySequence = deliverySequence;
	}
}