package com.sparta.cupeed.product.presentation.dto.response;

import com.sparta.cupeed.product.domain.model.Product;
import com.sparta.cupeed.product.domain.vo.ProductCategory;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class ProductGetResponseDtoV1 {

	private final ProductDto product;

	public static ProductGetResponseDtoV1 of(Product product) {
		return ProductGetResponseDtoV1.builder()
			.product(ProductDto.from(product))
			.build();
	}

	@Getter
	@Builder
	public static class ProductDto {
		private final UUID id;
		private final UUID companyId;
		private final UUID hubId;
		private final String name;
		private final ProductCategory category;
		private final String description;
		private final Long unitPrice;
		private final Long quantity;
		private final Instant createdAt;
		private final Instant updatedAt;

		public static ProductDto from(Product product) {
			return ProductDto.builder()
				.id(product.getId())
				.companyId(product.getCompanyId())
				.hubId(product.getHubId())
				.name(product.getName())
				.category(product.getCategory())
				.description(product.getDescription())
				.unitPrice(product.getUnitPrice())
				.quantity(product.getQuantity())
				.createdAt(product.getCreatedAt())
				.updatedAt(product.getUpdatedAt())
				.build();
		}
	}
}
