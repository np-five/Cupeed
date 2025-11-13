package com.sparta.cupeed.product.infrastructure.persistence.entity;

import java.util.UUID;

import com.sparta.cupeed.product.domain.vo.ProductCategory;

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
			throw new IllegalArgumentException("재고 수량은 0 이상이어야 합니다.");
		this.quantity = quantity;
	}

	public void increaseQuantity(Long amount) {
		if (amount < 0)
			throw new IllegalArgumentException("증가량은 0 이상이어야 합니다.");
		updateQuantity(this.quantity + amount);
	}

	public void decreaseQuantity(Long amount) {
		if (amount < 0)
			throw new IllegalArgumentException("감소량은 0 이상이어야 합니다.");
		if (this.quantity - amount < 0)
			throw new IllegalArgumentException("재고는 0 미만이 될 수 없습니다.");
		updateQuantity(this.quantity - amount);
	}

}
