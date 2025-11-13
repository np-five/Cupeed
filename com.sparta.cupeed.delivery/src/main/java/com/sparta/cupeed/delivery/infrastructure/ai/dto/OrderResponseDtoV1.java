package com.sparta.cupeed.delivery.infrastructure.ai.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDtoV1 {

	private UUID orderId;
	private String orderNumber;
	private String recieveCompanyName;
	private Instant orderDate;
	private String customerRequest;
	private List<OrderItemDto> orderItemList;

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class OrderItemDto {
		private UUID productId;
		private String productName;
		private Long quantity;
		private Long unitPrice;
		private Long subtotal;
	}
}