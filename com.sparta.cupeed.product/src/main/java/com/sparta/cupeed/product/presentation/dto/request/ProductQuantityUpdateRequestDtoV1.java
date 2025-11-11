package com.sparta.cupeed.product.presentation.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductQuantityUpdateRequestDtoV1 {

	@NotNull(message = "변경할 수량을 입력해주세요.")
	@Min(value = 0, message = "수량은 0 이상이어야 합니다.")
	private Long quantity;
}
