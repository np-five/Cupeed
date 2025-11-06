package com.sparta.cupeed.product.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.sparta.cupeed.product.domain.entity.Product;
import com.sparta.cupeed.product.domain.vo.ProductCategory;

public record ProductResponseDto(
	UUID id,
	String name,
	ProductCategory category,
	long unitPrice,
	long quantity
) {
	public static ProductResponseDto fromEntity(Product product) {
		return new ProductResponseDto(
			product.getId(),
			product.getName(),
			product.getCategory(),
			product.getUnitPrice(),
			product.getQuantity()
		);
	}
}
