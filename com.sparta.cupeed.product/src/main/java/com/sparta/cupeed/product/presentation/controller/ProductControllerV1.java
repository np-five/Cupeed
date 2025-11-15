package com.sparta.cupeed.product.presentation.controller;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.sparta.cupeed.global.response.ApiResponse;
import com.sparta.cupeed.product.application.service.ProductServiceV1;
import com.sparta.cupeed.product.infrastructure.security.auth.UserDetailsImpl;
import com.sparta.cupeed.product.presentation.code.ProductSuccessCode;
import com.sparta.cupeed.product.presentation.dto.request.ProductPostRequestDtoV1;
import com.sparta.cupeed.product.presentation.dto.request.ProductQuantityUpdateRequestDtoV1;
import com.sparta.cupeed.product.presentation.dto.request.ProductStockRequestDtoV1;
import com.sparta.cupeed.product.presentation.dto.response.ProductPostResponseDtoV1;
import com.sparta.cupeed.product.presentation.dto.response.ProductGetResponseDtoV1;
import com.sparta.cupeed.product.presentation.dto.response.ProductsGetResponseDtoV1;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/products")
public class ProductControllerV1 {

	private final ProductServiceV1 productServiceV1;

	// 상품 생성
	@PostMapping
	public ResponseEntity<ApiResponse<Void>> createProduct(@RequestBody @Valid ProductPostRequestDtoV1 requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
		productServiceV1.createProduct(requestDto, userDetails);
		return ResponseEntity.ok(ApiResponse.success(ProductSuccessCode.CREATED));
	}

	// 상품 전체 조회
	@GetMapping
	public ResponseEntity<ApiResponse<ProductsGetResponseDtoV1>> getProducts(@PageableDefault(size = 10) Pageable pageable) {
		ProductsGetResponseDtoV1 response = productServiceV1.getProducts(pageable);
		return ResponseEntity.ok(ApiResponse.success(ProductSuccessCode.OK, response));
	}

	// 상품 단건 조회
	@GetMapping("/{productId}")
	public ResponseEntity<ApiResponse<ProductGetResponseDtoV1>> getProduct(@PathVariable UUID productId) {
		ProductGetResponseDtoV1 response = productServiceV1.getProduct(productId);
		return ResponseEntity.ok(ApiResponse.success(ProductSuccessCode.OK, response));
	}

	// 상품 수정
	@PutMapping("/{productId}")
	public ResponseEntity<ApiResponse<ProductPostResponseDtoV1>> updateProduct(@PathVariable UUID productId, @RequestBody @Valid ProductPostRequestDtoV1 request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
		ProductPostResponseDtoV1 response = productServiceV1.updateProduct(productId, request, userDetails);
		return ResponseEntity.ok(ApiResponse.success(ProductSuccessCode.OK, response));
	}

	// 상품 삭제
	@DeleteMapping("/{productId}")
	public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable UUID productId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
		productServiceV1.deleteProduct(productId, userDetails);
		return ResponseEntity.ok(ApiResponse.success(ProductSuccessCode.OK));
	}

	// 상품 재고 수정
	@PatchMapping("/{productId}/quantity")
	public ResponseEntity<ApiResponse<ProductPostResponseDtoV1>> updateProductQuantity(@PathVariable UUID productId, @RequestBody @Valid ProductQuantityUpdateRequestDtoV1 request, @AuthenticationPrincipal UserDetailsImpl userDetails) {
		ProductPostResponseDtoV1 response = productServiceV1.updateProductQuantity(productId, request, userDetails);
		return ResponseEntity.ok(ApiResponse.success(ProductSuccessCode.OK, response));
	}

	// 주문용: 재고 차감
	@PostMapping("/decrease-stock")
	public ResponseEntity<ApiResponse<Void>> decreaseStock(@RequestBody ProductStockRequestDtoV1 requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
		productServiceV1.decreaseStock(requestDto, userDetails);
		return ResponseEntity.ok(ApiResponse.success(ProductSuccessCode.OK));
	}

	// 주문 취소용: 재고 복원
	@PostMapping("/restore-stock")
	public ResponseEntity<ApiResponse<Void>> restoreStock(@RequestBody ProductStockRequestDtoV1 requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
		productServiceV1.increaseStock(requestDto, userDetails);
		return ResponseEntity.ok(ApiResponse.success(ProductSuccessCode.OK));
	}
}
