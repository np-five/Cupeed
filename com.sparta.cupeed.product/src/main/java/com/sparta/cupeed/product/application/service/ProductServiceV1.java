package com.sparta.cupeed.product.application.service;

import com.sparta.cupeed.product.domain.model.Product;
import com.sparta.cupeed.product.domain.repository.ProductRepository;
import com.sparta.cupeed.product.presentation.dto.request.ProductPostRequestDtoV1;
import com.sparta.cupeed.product.presentation.dto.request.ProductQuantityUpdateRequestDtoV1;
import com.sparta.cupeed.product.presentation.dto.response.ProductPostResponseDtoV1;
import com.sparta.cupeed.product.presentation.dto.response.ProductGetResponseDtoV1;
import com.sparta.cupeed.product.presentation.dto.response.ProductsGetResponseDtoV1;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceV1 {

	private final ProductRepository productRepository;

	@Transactional
	public ProductPostResponseDtoV1 createProduct(ProductPostRequestDtoV1 requestDto) {
		ProductPostRequestDtoV1.ProductDto dto = requestDto.getProduct();

		Product newProduct = Product.builder()
			.companyId(dto.getCompanyId())
			.hubId(dto.getHubId())
			.name(dto.getName())
			.category(dto.getCategory())
			.description(dto.getDescription())
			.unitPrice(dto.getUnitPrice())
			.quantity(dto.getQuantity())
			.build();

		Product savedProduct = productRepository.save(newProduct);

		return ProductPostResponseDtoV1.of(savedProduct); // 엔티티 → DTO 변환
	}

	@Transactional(readOnly = true)
	public ProductsGetResponseDtoV1 getProducts(Pageable pageable) {
		Page<Product> page = productRepository.findAll(pageable);
		return ProductsGetResponseDtoV1.of(page);
	}

	@Transactional(readOnly = true)
	public ProductGetResponseDtoV1 getProduct(UUID productId) {
		Product product = productRepository.findByIdOrElseThrow(productId);
		return ProductGetResponseDtoV1.of(product);
	}

	@Transactional
	public ProductPostResponseDtoV1 updateProduct(UUID productId, ProductPostRequestDtoV1 requestDto) {
		Product product = productRepository.findByIdOrElseThrow(productId);
		ProductPostRequestDtoV1.ProductDto dto = requestDto.getProduct();

		Product updated = product.withUpdatedInfo(
			dto.getName(),
			dto.getCategory(),
			dto.getDescription(),
			dto.getUnitPrice()
		);

		productRepository.save(updated);
		return ProductPostResponseDtoV1.of(updated);
	}

	// TODO deleteBy 인증인가가 들어오면 구현
	@Transactional
	public void deleteProduct(UUID productId) {
		Product product = productRepository.findByIdOrElseThrow(productId);
		Product deleted = product.markDeleted();
		productRepository.save(deleted);
	}

	@Transactional
	public ProductPostResponseDtoV1 updateProductQuantity(UUID productId, ProductQuantityUpdateRequestDtoV1 requestDto) {
		Product product = productRepository.findByIdOrElseThrow(productId);
		Product updated = product.withQuantity(requestDto.getQuantity());
		productRepository.save(updated);
		return ProductPostResponseDtoV1.of(updated);
	}

	// 주문 서버용: 수량 지정 재고 차감
	@Transactional
	public void decreaseProductQuantityByAmount(UUID productId, Long quantity) {
		Product product = productRepository.findByIdOrElseThrow(productId);
		Product updated = product.decreaseQuantity(quantity);
		productRepository.save(updated);
	}

	// 주문 서버용: 수량 지정 재고 복원
	@Transactional
	public void increaseProductQuantityByAmount(UUID productId, Long quantity) {
		Product product = productRepository.findByIdOrElseThrow(productId);
		Product updated = product.increaseQuantity(quantity);
		productRepository.save(updated);
	}
}
