package com.sparta.cupeed.product.application.command;

import java.util.UUID;
import com.sparta.cupeed.product.domain.vo.ProductCategory;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// 상품 등록을 위한 Command 객체 (사용자의 의도를 표현)
public record ProductCreateCommand(
	@NotNull(message = "업체 ID는 필수입니다.")
	UUID companyId,

	@NotNull(message = "허브 ID는 필수입니다.")
	UUID hubId,

	@NotBlank(message = "상품 이름은 필수 항목입니다.")
	String name,

	@NotNull(message = "카테고리는 필수입니다.")
	ProductCategory category,

	String description, // 선택 입력 가능

	@Min(value = 0, message = "단가는 0원 이상이어야 합니다.")
	long unitPrice,

	@Min(value = 0, message = "초기 재고는 0개 이상이어야 합니다.")
	long initialQuantity,

	@NotBlank(message = "생성자 정보는 필수입니다.")
	String createdBy
) { }
