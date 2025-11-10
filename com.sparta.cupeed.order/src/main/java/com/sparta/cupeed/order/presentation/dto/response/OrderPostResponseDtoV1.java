package com.sparta.cupeed.order.presentation.dto.response;

import com.sparta.cupeed.order.domain.model.Order;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderPostResponseDtoV1 {
	private final OrderDto order;

	public static OrderPostResponseDtoV1 of(Order order) {
		return OrderPostResponseDtoV1.builder()
			.order(OrderDto.from(order))
			.build();
	}

	@Getter
	@Builder
	public static class OrderDto {

		private final String id;

		public static OrderDto from(Order order) {
			return OrderDto.builder()
				.id(String.valueOf(order.getId()))
				.build();
		}
	}
}
