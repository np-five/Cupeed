package com.sparta.cupeed.product.presentation;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.cupeed.product.application.ProductService;
import com.sparta.cupeed.product.application.command.ProductCreateCommand;
import com.sparta.cupeed.product.application.dto.ProductResponseDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/products")
public class ProductController {

	private final ProductService productService;

	@PostMapping
	// 반환 타입은 등록된 상품의 ID 또는 DTO가 될 수 있음
	public ResponseEntity<ProductResponseDto> createProduct(@RequestBody ProductCreateCommand command) {
		// Controller는 요청을 Command로 받아 Application Service에 전달
		return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(command));
	}
}