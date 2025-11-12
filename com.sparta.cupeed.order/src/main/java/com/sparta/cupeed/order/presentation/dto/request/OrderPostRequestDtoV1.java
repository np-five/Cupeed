package com.sparta.cupeed.order.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
@Schema(description = "주문 생성 요청 DTO")
public class OrderPostRequestDtoV1 {

	@NotNull(message = "주문 정보를 입력해주세요.")
	@Valid
	@Schema(description = "주문 정보", required = true)
	private OrderDto order;

	@Getter
	@Builder
	@Schema(description = "주문 상세 정보 DTO")
	public static class OrderDto {

		@NotNull(message = "주문 상품 목록을 입력해주세요.")
		@Size(min = 1, message = "주문 상품을 1개 이상 입력해주세요.")
		@Valid
		@Schema(description = "주문 상품 목록", required = true)
		private List<OrderItemDto> orderItemList;

		@Getter
		@Builder
		@Schema(description = "주문 상품 DTO")
		public static class OrderItemDto {

			@NotNull(message = "상품 ID를 입력해주세요.")
			@Schema(description = "상품 ID", example = "d290f1ee-6c54-4b01-90e6-d701748f0851", required = true)
			private UUID productId;

			@NotNull(message = "수량을 입력해주세요.")
			@Min(value = 1, message = "수량은 1 이상이어야 합니다.")
			@Schema(description = "상품 수량", example = "2", required = true)
			private Long quantity;
		}

	}
}
