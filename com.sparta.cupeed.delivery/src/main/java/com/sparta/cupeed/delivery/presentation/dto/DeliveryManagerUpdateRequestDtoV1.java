package com.sparta.cupeed.delivery.presentation.dto;

import java.util.UUID;

import com.sparta.cupeed.delivery.domain.model.DeliveryType;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryManagerUpdateRequestDtoV1 {

	private UUID hubId;

	private DeliveryType deliveryType;

	@Min(value = 0, message = "배송 순번은 0 이상이어야 합니다")
	private Integer deliverySequence;
}