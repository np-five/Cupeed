package com.sparta.cupeed.order.infrastructure.product.dto.response;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductsGetResponseDtoV1 {
	private ProductPageDto productPage;

	@Getter
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	@Builder
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class ProductPageDto {

		private List<ProductDto> content;
	}

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
