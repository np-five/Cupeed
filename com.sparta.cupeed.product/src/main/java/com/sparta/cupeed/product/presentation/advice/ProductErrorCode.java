package com.sparta.cupeed.product.presentation.advice;

import org.springframework.http.HttpStatus;
import com.sparta.cupeed.global.exception.ErrorCode;

public enum ProductErrorCode implements ErrorCode {

	PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "P001", "상품을 찾을 수 없습니다."),
	INVALID_PRODUCT_STATUS(HttpStatus.BAD_REQUEST, "P002", "잘못된 상품 상태입니다."),
	OUT_OF_STOCK(HttpStatus.BAD_REQUEST, "P003", "재고가 부족합니다.");

	private final HttpStatus status;
	private final String code;
	private final String message;

	ProductErrorCode(HttpStatus status, String code, String message) {
		this.status = status;
		this.code = code;
		this.message = message;
	}

	@Override
	public HttpStatus getStatus() {
		return status;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
