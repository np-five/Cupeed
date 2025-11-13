package com.sparta.cupeed.product.presentation.dto.response;

import com.sparta.cupeed.product.domain.model.Product;
import com.sparta.cupeed.product.domain.vo.ProductCategory;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Builder
public class ProductsGetResponseDtoV1 {

	private final List<ProductDto> products;
	private final long totalElements;
	private final int totalPages;

	public static ProductsGetResponseDtoV1 of(Page<Product> page) {
		List<ProductDto> productList = page.getContent().stream()
			.map(ProductDto::from)
			.collect(Collectors.toList());

		return ProductsGetResponseDtoV1.builder()
			.products(productList)
			.totalElements(page.getTotalElements())
			.totalPages(page.getTotalPages())
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
