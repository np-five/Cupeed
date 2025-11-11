package com.sparta.cupeed.product.domain.model;

import com.sparta.cupeed.product.domain.vo.ProductCategory;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
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
	public Product withUpdatedInfo(String name,
		ProductCategory category,
		String description,
		Long unitPrice) {
		return toBuilder()
			.name(name != null ? name : this.name)
			.category(category != null ? category : this.category)
			.description(description != null ? description : this.description)
			.unitPrice(unitPrice != null ? unitPrice : this.unitPrice)
			.updatedAt(Instant.now())
			.build();
	}

	// 재고 수량 변경
	public Product withQuantity(Long quantity) {
		if (quantity < 0) throw new IllegalArgumentException("재고 수량은 0 이상이어야 합니다.");
		return toBuilder()
			.quantity(quantity)
			.updatedAt(Instant.now())
			.build();
	}

	public Product increaseQuantity(Long amount) {
		if (amount < 0) throw new IllegalArgumentException("증가량은 0 이상이어야 합니다.");
		return withQuantity(this.quantity + amount);
	}

	public Product decreaseQuantity(Long amount) {
		if (amount < 0) throw new IllegalArgumentException("감소량은 0 이상이어야 합니다.");
		if (this.quantity - amount < 0) throw new IllegalArgumentException("재고는 0 미만이 될 수 없습니다.");
		return withQuantity(this.quantity - amount);
	}

	// 논리 삭제
	public Product markDeleted() {
		return toBuilder()
			.deletedAt(Instant.now())
			.deletedBy("system") // 임시 값
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
		Objects.requireNonNull(companyId, "회사 ID는 필수입니다.");
		Objects.requireNonNull(hubId, "허브 ID는 필수입니다.");
		Objects.requireNonNull(name, "상품명은 필수입니다.");
		if (unitPrice < 0) throw new IllegalArgumentException("단가는 0 이상이어야 합니다.");
		if (initialQuantity < 0) throw new IllegalArgumentException("재고 수량은 0 이상이어야 합니다.");

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
