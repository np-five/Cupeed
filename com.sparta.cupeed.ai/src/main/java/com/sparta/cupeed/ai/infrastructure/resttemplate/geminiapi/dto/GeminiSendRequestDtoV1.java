package com.sparta.cupeed.ai.infrastructure.resttemplate.geminiapi.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "Gemini API 요청 DTO")
public class GeminiSendRequestDtoV1 {

	@Schema(description = "주문 ID", example = "8a1e7b22-2134-4f1e-9b56-1e3d2f9d3a6b")
	private final UUID orderId;

	@Schema(description = "주문 번호", example = "ORD-20251111-001")
	private final String orderNumber;

	@Schema(description = "수령 업체명", example = "스파르타물류센터")
	private final String recieveCompanyName;

	@Schema(description = "주문 일시(UTC)", example = "2025-11-11T03:00:00Z")
	private final Instant orderDate;

	@Schema(description = "주문 상품 목록")
	private final List<ProductInfo> products;

	@Schema(description = "고객 요청사항", example = "배송 전 연락 바랍니다.")
	private final String customerRequest;

	@Schema(description = "배송 담당자명", example = "김철수")
	private final String deliveryManagerName;

	@Schema(description = "출발 허브명", example = "서울 물류센터")
	private final String startHubName;

	@Schema(description = "도착 허브명", example = "부산 허브터미널")
	private final String endHubName;

	@Getter
	@Builder
	@Schema(description = "상품 정보")
	public static class ProductInfo {

		@Schema(description = "상품명", example = "무선 키보드")
		private final String productName;

		@Schema(description = "수량", example = "3")
		private final Long quantity;
	}
}
