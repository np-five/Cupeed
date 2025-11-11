package com.sparta.cupeed.product.infrastructure.persistence.mapper;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.sparta.cupeed.product.domain.model.Product;
import com.sparta.cupeed.product.infrastructure.persistence.entity.ProductEntity;

@Component
public class ProductMapper {

	// ProductEntity -> Product 도메인 변환
	public Product toDomain(ProductEntity entity) {
		if (entity == null) return null;

		return Product.builder()
			.id(entity.getId())
			.companyId(entity.getCompanyId())
			.hubId(entity.getHubId())
			.name(entity.getName())
			.category(entity.getCategory())
			.description(entity.getDescription())
			.unitPrice(entity.getUnitPrice())
			.quantity(entity.getQuantity())
			.createdAt(entity.getCreatedAt())
			.createdBy(entity.getCreatedBy())
			.updatedAt(entity.getUpdatedAt())
			.updatedBy(entity.getUpdatedBy())
			.deletedAt(entity.getDeletedAt())
			.deletedBy(entity.getDeletedBy())
			.build();
	}

	// Product 도메인 -> ProductEntity 변환
	public ProductEntity toEntity(Product domain) {
		if (domain == null) return null;

		ProductEntity entity = ProductEntity.builder()
			.id(domain.getId()) // 신규 저장이면 null일 수 있음
			.companyId(domain.getCompanyId())
			.hubId(domain.getHubId())
			.name(domain.getName())
			.category(domain.getCategory())
			.description(domain.getDescription())
			.unitPrice(domain.getUnitPrice())
			.quantity(domain.getQuantity())
			.build();

		return entity;
	}

	// 도메인의 변경 내용을 엔티티에 반영
	public void applyDomain(Product domain, ProductEntity entity) {
		if (domain == null || entity == null) return;

		entity.updateInfo(domain.getName(), domain.getCategory(), domain.getDescription(), domain.getUnitPrice());
		entity.updateQuantity(domain.getQuantity());

		if (domain.getDeletedAt() != null && domain.getDeletedBy() != null) {
			entity.markDeleted(domain.getDeletedAt(), domain.getDeletedBy());
		}
	}
}
