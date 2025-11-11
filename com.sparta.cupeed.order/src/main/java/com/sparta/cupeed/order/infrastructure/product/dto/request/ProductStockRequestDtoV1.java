package com.sparta.cupeed.order.infrastructure.product.dto.request;

import java.util.List;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductStockRequestDtoV1 {
	private final OrderDto order;
	private final List<ProductStockDto> productStocks;

	@Getter
	@Builder
	public static class OrderDto {
		private final UUID orderId;
	}

	@Getter
	@Builder
	public static class ProductStockDto {
		private final UUID productId;
		private final Long quantity;
	}
}
