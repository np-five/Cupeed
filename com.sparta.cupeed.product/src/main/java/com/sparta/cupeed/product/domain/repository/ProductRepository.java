package com.sparta.cupeed.product.domain.repository;

import java.util.Optional;
import java.util.UUID;

import com.sparta.cupeed.product.domain.entity.Product;

// 순수한 Domain 계약 (인터페이스)
public interface ProductRepository {

	void save(Product product);
	Optional<Product> findById(UUID id);
	boolean existsByName(String name); // 상품 이름 중복 검사 등

	// 도메인 예외를 던지는 default 메서드는 Domain Service로 분리하는 것이 더 좋음
	default Product findByIdOrElseThrow(UUID id) {
		// return findById(id).orElseThrow(() -> new DomainException(...));
		return findById(id).get(); // 임시
	}
}