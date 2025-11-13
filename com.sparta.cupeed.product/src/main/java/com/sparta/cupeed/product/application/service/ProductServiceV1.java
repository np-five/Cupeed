package com.sparta.cupeed.product.application.service;

import com.sparta.cupeed.product.domain.model.Product;
import com.sparta.cupeed.product.domain.repository.ProductRepository;
import com.sparta.cupeed.product.infrastructure.company.client.CompanyClientV1;
import com.sparta.cupeed.product.infrastructure.security.auth.UserDetailsImpl;
import com.sparta.cupeed.product.infrastructure.user.client.UserClientV1;
import com.sparta.cupeed.product.infrastructure.user.dto.response.InternalUserResponseDtoV1;
import com.sparta.cupeed.product.presentation.dto.request.ProductPostRequestDtoV1;
import com.sparta.cupeed.product.presentation.dto.request.ProductQuantityUpdateRequestDtoV1;
import com.sparta.cupeed.product.presentation.dto.request.ProductStockRequestDtoV1;
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
	private final UserClientV1 userClient;
	private final CompanyClientV1 companyClient;

	@Transactional
	public ProductPostResponseDtoV1 createProduct(ProductPostRequestDtoV1 requestDto, UserDetailsImpl userDetails) {

		ProductPostRequestDtoV1.ProductDto dto = requestDto.getProduct();

		InternalUserResponseDtoV1 findUser = userClient.getInternalUser(userDetails.getId());
		if (findUser == null) {
			throw new IllegalArgumentException("유효하지 않은 유저 ID입니다.");
		}

		UUID findHunId = companyClient.getCompany(findUser.getUser().getCompanyId());
		if (findHunId == null) {
			throw new IllegalArgumentException("유효하지 않은 허브 ID입니다.");
		}

		// 2️⃣ Product 엔티티 생성
		Product newProduct = Product.builder()
			.companyId(findUser.getUser().getCompanyId())
			.hubId(findHunId)
			.name(dto.getName())
			.category(dto.getCategory())
			.description(dto.getDescription())
			.unitPrice(dto.getUnitPrice())
			.quantity(dto.getQuantity())
			.build();

		Product savedProduct = productRepository.save(newProduct);
		return ProductPostResponseDtoV1.of(savedProduct);
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
		var dto = requestDto.getProduct();

		Product updated = product.withUpdatedInfo(
			dto.getName(),
			dto.getCategory(),
			dto.getDescription(),
			dto.getUnitPrice()
		);

		productRepository.save(updated);
		return ProductPostResponseDtoV1.of(updated);
	}

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

	@Transactional
	public void decreaseStock(ProductStockRequestDtoV1 requestDto) {
		for (ProductStockRequestDtoV1.ProductStockDto item : requestDto.getProductStocks()) {
			Product product = productRepository.findByIdOrElseThrow(item.getProductId());
			productRepository.save(product.decreaseQuantity(item.getQuantity()));
		}
	}

	@Transactional
	public void increaseStock(ProductStockRequestDtoV1 requestDto) {
		for (ProductStockRequestDtoV1.ProductStockDto item : requestDto.getProductStocks()) {
			Product product = productRepository.findByIdOrElseThrow(item.getProductId());
			productRepository.save(product.increaseQuantity(item.getQuantity()));
		}
	}
}
