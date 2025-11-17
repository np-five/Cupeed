package com.sparta.cupeed.product.application.service;

import com.sparta.cupeed.global.exception.BizException;
import com.sparta.cupeed.product.domain.model.Product;
import com.sparta.cupeed.product.domain.repository.ProductRepository;
import com.sparta.cupeed.product.infrastructure.company.client.CompanyClientV1;
import com.sparta.cupeed.product.infrastructure.persistence.repository.ProductRepositoryImpl;
import com.sparta.cupeed.product.infrastructure.security.auth.UserDetailsImpl;
import com.sparta.cupeed.product.infrastructure.user.client.UserClientV1;
import com.sparta.cupeed.product.infrastructure.user.dto.response.InternalUserResponseDtoV1;
import com.sparta.cupeed.product.presentation.code.ProductErrorCode;
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

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceV1 {

	private final ProductRepositoryImpl productRepository;
	private final UserClientV1 userClient;
	private final CompanyClientV1 companyClient;

	@Transactional
	public ProductPostResponseDtoV1 createProduct(ProductPostRequestDtoV1 requestDto, UserDetailsImpl userDetails) {

		InternalUserResponseDtoV1 findUser = userClient.getInternalUser(userDetails.getId());
		if (findUser == null) {
			throw new BizException(ProductErrorCode.INVALID_USER);
		}

		UUID findHunId = companyClient.getCompany(findUser.getUser().getCompanyId());
		if (findHunId == null) {
			throw new BizException(ProductErrorCode.INVALID_HUB);
		}

		Product newProduct = Product.builder()
			.companyId(findUser.getUser().getCompanyId())
			.hubId(findHunId)
			.name(requestDto.getProduct().getName())
			.category(requestDto.getProduct().getCategory())
			.description(requestDto.getProduct().getDescription())
			.unitPrice(requestDto.getProduct().getUnitPrice())
			.quantity(requestDto.getProduct().getQuantity())
			.createdBy(String.valueOf(userDetails.getId()))
			.updatedBy(String.valueOf(userDetails.getId()))
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
	public ProductPostResponseDtoV1 updateProduct(UUID productId, ProductPostRequestDtoV1 requestDto,  UserDetailsImpl userDetails) {
		Product product = productRepository.findByIdOrElseThrow(productId);

		Product updated = product.withUpdatedInfo(
			requestDto.getProduct().getName(),
			requestDto.getProduct().getCategory(),
			requestDto.getProduct().getDescription(),
			requestDto.getProduct().getUnitPrice(),
			requestDto.getProduct().getQuantity(),
			userDetails.getUserId()
		);

		productRepository.save(updated);
		return ProductPostResponseDtoV1.of(updated);
	}

	@Transactional
	public void deleteProduct(UUID productId, UserDetailsImpl userDetails) {
		Product product = productRepository.findByIdOrElseThrow(productId);
		Product deleted = product.markDeleted(String.valueOf(userDetails.getId()));
		productRepository.save(deleted);
	}

	@Transactional
	public ProductPostResponseDtoV1 updateProductQuantity(UUID productId, ProductQuantityUpdateRequestDtoV1 requestDto, UserDetailsImpl userDetails) {
		Product product = productRepository.findByIdOrElseThrow(productId);
		Product updated = product.withQuantity(requestDto.getQuantity(), String.valueOf(userDetails.getId()));
		productRepository.save(updated);
		return ProductPostResponseDtoV1.of(updated);
	}

	@Transactional
	public void decreaseStock(ProductStockRequestDtoV1 requestDto, UserDetailsImpl userDetails) {
		// 1. 요청된 상품 ID들을 모두 추출
		List<UUID> productIds = requestDto.getProductStocks().stream()
			.map(ProductStockRequestDtoV1.ProductStockDto::getProductId)
			.toList();

		// 2. 한 번에 조회
		List<Product> products = productRepository.findAllById(productIds);

		// 3. DTO와 맵핑해서 재고 차감
		Map<UUID, Long> quantityMap = requestDto.getProductStocks().stream()
			.collect(Collectors.toMap(
				ProductStockRequestDtoV1.ProductStockDto::getProductId,
				ProductStockRequestDtoV1.ProductStockDto::getQuantity,
				(existing, replacement) -> existing // 혹시 중복될 경우 기존 값 유지
			));

		List<Product> updatedProducts = products.stream()
			.map(product -> {
				Long qty = quantityMap.get(product.getId());
				if (qty == null) {
					throw new BizException(ProductErrorCode.INVALID_QUANTITY);
				}
				return product.decreaseQuantity(qty, String.valueOf(userDetails.getId()));
			})
			.toList();

		// 4. 한 번에 저장
		productRepository.saveAll(updatedProducts);
	}

	@Transactional
	public void increaseStock(ProductStockRequestDtoV1 requestDto, UserDetailsImpl userDetails) {
		List<UUID> productIds = requestDto.getProductStocks().stream()
			.map(ProductStockRequestDtoV1.ProductStockDto::getProductId)
			.toList();

		List<Product> products = productRepository.findAllById(productIds);

		Map<UUID, Long> quantityMap = requestDto.getProductStocks().stream()
			.collect(Collectors.toMap(
				ProductStockRequestDtoV1.ProductStockDto::getProductId,
				ProductStockRequestDtoV1.ProductStockDto::getQuantity,
				(existing, replacement) -> existing
			));

		List<Product> updatedProducts = products.stream()
			.map(product -> {
				Long qty = quantityMap.get(product.getId());
				if (qty == null) {
					throw new BizException(ProductErrorCode.INVALID_QUANTITY);
				}
				return product.increaseQuantity(qty, String.valueOf(userDetails.getId()));
			})
			.toList();

		productRepository.saveAll(updatedProducts);
	}
}
