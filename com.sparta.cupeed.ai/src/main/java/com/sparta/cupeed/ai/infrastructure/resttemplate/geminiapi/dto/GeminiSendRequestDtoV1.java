package com.sparta.cupeed.ai.infrastructure.resttemplate.geminiapi.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GeminiSendRequestDtoV1 {
	private final UUID orderId;
	private final String orderNumber;
	private final String recieveCompanyName;
	private final Instant orderDate;
	private final List<ProductInfo> products;
	private final String customerRequest;
	private final String deliveryManagerName;
	private final String startHubName;
	private final String endHubName;

	@Getter
	@Builder
	public static class ProductInfo {
		private final String productName;
		private final Long quantity;
	}
}
