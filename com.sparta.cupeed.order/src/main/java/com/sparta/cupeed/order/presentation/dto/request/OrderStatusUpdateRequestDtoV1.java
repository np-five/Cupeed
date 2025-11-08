package com.sparta.cupeed.order.presentation.dto.request;

import com.sparta.cupeed.order.domain.model.Order;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderStatusUpdateRequestDtoV1 {
	@NotNull(message = "주문 상태를 입력해주세요.")
	private Order.Status status;
}
