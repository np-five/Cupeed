package com.sparta.cupeed.order.infrastructure.resttemplate.product.dto.response;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ProductGetResponseDtoV1 {
	private ProductDto product;

	@Getter
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	@Builder
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class ProductDto {

		private String id;
		private String productName;
		private BigDecimal unitPrice;
		private Long stock;
	}
}
