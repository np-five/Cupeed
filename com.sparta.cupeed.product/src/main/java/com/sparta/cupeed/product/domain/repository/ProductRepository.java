package com.sparta.cupeed.product.domain.repository;

import com.sparta.cupeed.product.domain.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository {

	Product save(Product product);

	Optional<Product> findById(UUID id);

	boolean existsByName(String name);

	Page<Product> findAll(Pageable pageable); // Page 조회 추가

	default Product findByIdOrElseThrow(UUID id) {
		return findById(id)
			.orElseThrow(() -> new RuntimeException("Product not found: " + id));
	}
}
