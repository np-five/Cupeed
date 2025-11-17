package com.sparta.cupeed.product.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.cupeed.global.response.ApiResponse;
import com.sparta.cupeed.product.application.service.ProductServiceV1;
import com.sparta.cupeed.product.infrastructure.security.auth.UserDetailsImpl;
import com.sparta.cupeed.product.presentation.code.ProductSuccessCode;
import com.sparta.cupeed.product.presentation.dto.request.ProductStockRequestDtoV1;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/v1/products")
public class ProductInternalControllerV1 {

	private final ProductServiceV1 productServiceV1;

	// 주문용: 재고 차감
	@PostMapping("/{productId}/decrease-stock")
	public ResponseEntity<ApiResponse<Void>> decreaseStock(@RequestBody ProductStockRequestDtoV1 requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
		productServiceV1.decreaseStock(requestDto, userDetails);
		return ResponseEntity.ok(ApiResponse.success(ProductSuccessCode.OK));
	}

	// 주문 취소용: 재고 복원
	@PostMapping("/{productId}/restore-stock")
	public ResponseEntity<ApiResponse<Void>> restoreStock(@RequestBody ProductStockRequestDtoV1 requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
		productServiceV1.increaseStock(requestDto, userDetails);
		return ResponseEntity.ok(ApiResponse.success(ProductSuccessCode.OK));
	}
}
