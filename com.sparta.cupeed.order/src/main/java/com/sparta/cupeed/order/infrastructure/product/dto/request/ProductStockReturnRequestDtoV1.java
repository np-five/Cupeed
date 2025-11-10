package com.sparta.cupeed.order.infrastructure.product.dto.request;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductStockReturnRequestDtoV1 {
	private final OrderDto order;

	@Getter
	@Builder
	public static class OrderDto {
		private final UUID orderId;
	}
}
