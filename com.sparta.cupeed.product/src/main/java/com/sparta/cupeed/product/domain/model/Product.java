package com.sparta.cupeed.product.domain.model;

import com.sparta.cupeed.product.domain.vo.ProductCategory;
import com.sparta.cupeed.product.presentation.code.ProductErrorCode;
import com.sparta.cupeed.global.exception.BizException;

import java.time.Instant;
import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode(of = "id")
public class Product {

	private final UUID id;
	private final UUID companyId;
	private final UUID hubId;
	private final String name;
	private final ProductCategory category;
	private final String description;
	private final Long unitPrice;
	private final Long quantity;
	private final Instant createdAt;
	private final String createdBy;
	private final Instant updatedAt;
	private final String updatedBy;
	private final Instant deletedAt;
	private final String deletedBy;

	// 불변 객체 유지용 toBuilder()
	ProductBuilder toBuilder() {
		return Product.builder()
			.id(id)
			.companyId(companyId)
			.hubId(hubId)
			.name(name)
			.category(category)
			.description(description)
			.unitPrice(unitPrice)
			.quantity(quantity)
			.createdAt(createdAt)
			.createdBy(createdBy)
			.updatedAt(updatedAt)
			.updatedBy(updatedBy)
			.deletedAt(deletedAt)
			.deletedBy(deletedBy);
	}

	// 상품 정보 수정
	public Product withUpdatedInfo(
			String name,
			ProductCategory category,
			String description,
			@NotNull(message = "단가를 입력해주세요.") @Min(value = 0, message = "단가는 0 이상이어야 합니다.") Long price,
			Long unitPrice, String userId
	) {
		if (unitPrice != null && unitPrice < 0) {
			throw new BizException(ProductErrorCode.INVALID_UNIT_PRICE);
		}
		return toBuilder()
			.name(name != null ? name : this.name)
			.category(category != null ? category : this.category)
			.description(description != null ? description : this.description)
			.unitPrice(unitPrice != null ? unitPrice : this.unitPrice)
			.updatedAt(Instant.now())
			.updatedBy(userId)
			.build();
	}

	// 재고 수량 변경
	public Product withQuantity(Long quantity, String userId) {
		if (quantity < 0) throw new BizException(ProductErrorCode.INVALID_QUANTITY);
		return toBuilder()
			.quantity(quantity)
			.updatedAt(Instant.now())
			.updatedBy(userId)
			.build();
	}

	public Product increaseQuantity(Long amount, String userId) {
		if (amount < 0) throw new BizException(ProductErrorCode.NEGATIVE_INCREASE);
		return withQuantity(this.quantity + amount, userId);
	}

	public Product decreaseQuantity(Long amount, String userId) {
		if (amount < 0) throw new BizException(ProductErrorCode.NEGATIVE_DECREASE);
		if (this.quantity - amount < 0) throw new BizException(ProductErrorCode.OUT_OF_STOCK);
		return withQuantity(this.quantity - amount, userId);
	}

	// 논리 삭제
	public Product markDeleted(String userId) {
		return toBuilder()
			.deletedAt(Instant.now())
			.deletedBy(userId)
			.build();
	}

	// 생성 팩토리
	public static Product create(
		UUID companyId,
		UUID hubId,
		String name,
		ProductCategory category,
		String description,
		Long unitPrice,
		Long initialQuantity
	) {
		if (companyId == null) throw new BizException(ProductErrorCode.MISSING_COMPANY_ID);
		if (hubId == null) throw new BizException(ProductErrorCode.MISSING_HUB_ID);
		if (name == null) throw new BizException(ProductErrorCode.MISSING_PRODUCT_NAME);
		if (unitPrice < 0) throw new BizException(ProductErrorCode.INVALID_UNIT_PRICE);
		if (initialQuantity < 0) throw new BizException(ProductErrorCode.INVALID_QUANTITY);

		return Product.builder()
			.id(UUID.randomUUID())
			.companyId(companyId)
			.hubId(hubId)
			.name(name)
			.category(category)
			.description(description)
			.unitPrice(unitPrice)
			.quantity(initialQuantity)
			.createdAt(Instant.now())
			.build();
	}

}
