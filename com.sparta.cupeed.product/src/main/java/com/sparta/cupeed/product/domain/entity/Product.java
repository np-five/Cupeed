package com.sparta.cupeed.product.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import com.sparta.cupeed.product.domain.vo.ProductCategory;
import jakarta.persistence.Column;
import jakarta.persistence.Convert; // Enum을 String으로 저장할 경우 필요
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType; // EnumType.STRING을 사용하지 않을 경우
import jakarta.persistence.Enumerated; // Enum 매핑용
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id; // PK 지정
import jakarta.persistence.Table; // 테이블명 지정 (선택적)
import jakarta.persistence.Temporal; // 시간 타입 지정
import jakarta.persistence.TemporalType;
import lombok.AccessLevel; // NoArgsConstructor의 접근 레벨 지정
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator; // UUID 자동 생성 전략
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@Entity
@Table(name = "p_products") // 실제 테이블명 지정
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE) // 생성자는 private으로 제한
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA용 protected 생성자
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(name = "company_id") // FK 역할, Nullable
	private UUID companyId;

	@Column(name = "hub_id") // FK 역할, Nullable
	private UUID hubId;

	@Column(name = "name", nullable = false) // NOT NULL, VARCHAR(255)
	private String name;

	@Enumerated(EnumType.STRING) // Enum을 DB에 문자열로 저장
	@Column(name = "category")
	private ProductCategory category;

	@Column(name = "description", columnDefinition = "TEXT")
	private String description;

	@Column(name = "unit_price") // 단가
	private long unitPrice;

	@Column(name = "quantity", nullable = false) // 재고 수량, NOT NULL
	private long quantity;

	@CreatedDate
	@Column(updatable = false)
	private LocalDateTime createdAt;

	@CreatedBy
	private Long createdBy;

	@LastModifiedDate
	private LocalDateTime updatedAt;

	@LastModifiedBy
	private Long updatedBy;

	private LocalDateTime deletedAt;

	private Long deletedBy;

	// **핵심: 생성 로직을 Entity 내부의 정적 팩토리 메서드로 캡슐화**
	// (기존 코드 유지 및 도메인 검증 강화)
	public static Product create(
		UUID companyId, UUID hubId, String name, ProductCategory category,
		String description, long unitPrice, long initialQuantity, String createdBy) {

		// 도메인 불변성 규칙 검증
		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException("상품 이름은 필수입니다.");
		}
		if (unitPrice < 0) {
			throw new IllegalArgumentException("단가는 0 이상이어야 합니다.");
		}

		// Builder를 사용하여 객체 생성 (Lombok @Builder 사용)
		return Product.builder()
			.companyId(companyId)
			.hubId(hubId)
			.name(name)
			.category(category)
			.description(description)
			.unitPrice(unitPrice)
			.quantity(initialQuantity)
			.build();
	}

	// ... 나머지 비즈니스 메서드 (increaseStock 등)
}