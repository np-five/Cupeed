package com.sparta.cupeed.order.presentation.dto.request;

import com.sparta.cupeed.order.domain.model.Order;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "주문 상태 수정 요청 DTO")
public class OrderStatusUpdateRequestDtoV1 {

	@NotNull(message = "주문 상태를 입력해주세요.")
	@Schema(
		description = "주문 상태",
		example = "COMPLETED",
		required = true,
		allowableValues = {"REQUESTED", "ACCEPTED", "SHIPPED", "DELIVERED", "CANCEL_REQUESTED", "CANCELED"}
	)
	private Order.Status status;
}
