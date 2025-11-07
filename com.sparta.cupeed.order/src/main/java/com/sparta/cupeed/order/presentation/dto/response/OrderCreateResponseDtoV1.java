package com.sparta.cupeed.order.presentation.dto.response;

import com.sparta.cupeed.order.domain.model.Order;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderCreateResponseDtoV1 {
	private final OrderDto order;

	public static OrderCreateResponseDtoV1 of(Order order) {
		return OrderCreateResponseDtoV1.builder()
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
