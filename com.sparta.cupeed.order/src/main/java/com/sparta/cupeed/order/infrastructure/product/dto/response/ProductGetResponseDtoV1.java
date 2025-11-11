package com.sparta.cupeed.order.infrastructure.product.dto.response;

import java.math.BigDecimal;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
public class ProductGetResponseDtoV1 {
	private ProductDto product;

	@Getter
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	@Builder
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class ProductDto {

		private String id;
		private String name;
		private UUID companyId;
		private BigDecimal unitPrice;
		private Long quantity;
	}
}
