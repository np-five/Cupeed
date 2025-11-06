package com.sparta.cupeed.product.infrastructure.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.sparta.cupeed.product.domain.entity.Product;
import com.sparta.cupeed.product.domain.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepository {

	private final ProductJpaRepository productJpaRepository; // 실제 JPA 레포지토리 주입

	// Domain Layer의 인터페이스 구현 (기술적 세부 구현)
	@Override
	public void save(Product product) {
		// JPA Entity로 변환 또는 직접 저장
		productJpaRepository.save(product);
	}

	@Override
	public Optional<Product> findById(UUID id) {
		return productJpaRepository.findById(id);
	}

	@Override
	public boolean existsByName(String name) {
		return productJpaRepository.existsByName(name);
	}
}
