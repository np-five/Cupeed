package com.sparta.cupeed.product.domain.repository;

import com.sparta.cupeed.global.exception.BizException;
import com.sparta.cupeed.product.domain.model.Product;
import com.sparta.cupeed.product.presentation.code.ProductErrorCode;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {

	Product save(Product product);

	Optional<Product> findById(UUID id);

	boolean existsByName(String name);

	Page<Product> findAll(Pageable pageable); // Page 조회 추가

	default Product findByIdOrElseThrow(UUID id) {
		return findById(id)
			.orElseThrow(() -> new BizException(ProductErrorCode.PRODUCT_NOT_FOUND));
	}

	List<Product> findAllById(List<UUID> productIds);

	void saveAll(List<Product> updatedProducts);
}
