package com.sparta.cupeed.delivery.infrastructure.ai.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GeminiSendRequestDtoV1 {
	private UUID orderId;
	private String orderNumber;
	private String recieveCompanyName;
	private Instant orderDate;
	private List<ProductInfo> products;
	private String customerRequest;
	private String deliveryManagerName;
	private String startHubName;
	private String endHubName;

	@Getter
	@Builder
	public static class ProductInfo {
		private String productName;
		private Long quantity;
	}
}
