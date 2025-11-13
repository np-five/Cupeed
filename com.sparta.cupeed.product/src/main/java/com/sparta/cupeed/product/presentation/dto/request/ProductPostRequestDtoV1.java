package com.sparta.cupeed.product.presentation.dto.request;

import com.sparta.cupeed.product.domain.vo.ProductCategory;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductPostRequestDtoV1 {

	@NotNull(message = "상품 정보를 입력해주세요.")
	@Valid
	private ProductDto product;

	@Getter
	@Builder
	public static class ProductDto {

		@NotBlank(message = "상품명을 입력해주세요.")
		private String name;

		@NotNull(message = "카테고리를 입력해주세요.")
		private ProductCategory category;

		private String description;

		@NotNull(message = "단가를 입력해주세요.")
		@Min(value = 0, message = "단가는 0 이상이어야 합니다.")
		private Long unitPrice;

		@NotNull(message = "재고 수량을 입력해주세요.")
		@Min(value = 0, message = "재고 수량은 0 이상이어야 합니다.")
		private Long quantity;
	}
}
