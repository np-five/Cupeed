package com.sparta.cupeed.product.presentation.code;

import org.springframework.http.HttpStatus;

import com.sparta.cupeed.global.code.ResponseCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductErrorCode implements ResponseCode {

	PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "PROD-001", "상품을 찾을 수 없습니다."),
	INVALID_PRODUCT_STATUS(HttpStatus.BAD_REQUEST, "PROD-002", "잘못된 상품 상태입니다."),
	OUT_OF_STOCK(HttpStatus.BAD_REQUEST, "PROD-003", "재고가 부족합니다."),
	INVALID_USER(HttpStatus.BAD_REQUEST, "PROD-004", "유효하지 않은 유저 ID입니다."),
	INVALID_HUB(HttpStatus.BAD_REQUEST, "PROD-005", "유효하지 않은 허브 ID입니다."),
	INVALID_UNIT_PRICE(HttpStatus.BAD_REQUEST, "PROD-006", "단가는 0 이상이어야 합니다."),
	INVALID_QUANTITY(HttpStatus.BAD_REQUEST, "PROD-007", "재고 수량은 0 이상이어야 합니다."),
	NEGATIVE_INCREASE(HttpStatus.BAD_REQUEST, "PROD-008", "증가량은 0 이상이어야 합니다."),
	NEGATIVE_DECREASE(HttpStatus.BAD_REQUEST, "PROD-009", "재고는 0 미만이 될 수 없습니다."),
	MISSING_COMPANY_ID(HttpStatus.BAD_REQUEST, "PROD-010", "회사 ID는 필수입니다."),
	MISSING_HUB_ID(HttpStatus.BAD_REQUEST, "PROD-011", "허브 ID는 필수입니다."),
	MISSING_PRODUCT_NAME(HttpStatus.BAD_REQUEST, "PROD-012", "상품명은 필수입니다.");

	private final HttpStatus status;
	private final String code;
	private final String message;

}
