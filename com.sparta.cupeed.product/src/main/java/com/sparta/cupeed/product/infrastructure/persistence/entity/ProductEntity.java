package com.sparta.cupeed.product.infrastructure.persistence.entity;

import java.util.UUID;

import com.sparta.cupeed.global.exception.BizException;
import com.sparta.cupeed.product.domain.vo.ProductCategory;
import com.sparta.cupeed.product.presentation.code.ProductErrorCode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "p_product")
public class ProductEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(name = "company_id", nullable = false)
	private UUID companyId;

	@Column(name = "hub_id", nullable = false)
	private UUID hubId;

	@Column(nullable = false)
	private String name;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ProductCategory category;

	@Column(columnDefinition = "TEXT")
	private String description;

	@Column(name = "unit_price", nullable = false)
	private Long unitPrice;

	@Column(nullable = false)
	private Long quantity;

	// 편의 메서드
	public void updateInfo(String name, ProductCategory category, String description, Long unitPrice) {
		if (name != null && !name.isBlank())
			this.name = name;
		if (category != null)
			this.category = category;
		if (description != null)
			this.description = description;
		if (unitPrice != null && unitPrice >= 0)
			this.unitPrice = unitPrice;
	}

	public void updateQuantity(Long quantity) {
		if (quantity == null || quantity < 0)
			throw new BizException(ProductErrorCode.INVALID_QUANTITY);
		this.quantity = quantity;
	}

}
