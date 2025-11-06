package com.sparta.cupeed.product.infrastructure.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sparta.cupeed.product.domain.entity.Product;
import com.sparta.cupeed.product.domain.repository.ProductRepository;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

	private final ProductJpaRepository jpaRepository; // 실제 JPA 레포지토리 주입

	public ProductRepositoryImpl(ProductJpaRepository jpaRepository) {
		this.jpaRepository = jpaRepository;
	}

	// Domain Layer의 인터페이스 구현 (기술적 세부 구현)
	@Override
	public void save(Product product) {
		// JPA Entity로 변환 또는 직접 저장
		jpaRepository.save(product);
	}

	@Override
	public Optional<Product> findById(UUID id) {
		return jpaRepository.findById(id);
	}

	@Override
	public boolean existsByName(String name) {
		return jpaRepository.existsByName(name);
	}
}

// JpaRepository는 Infrastructure Layer의 세부 구현체
interface ProductJpaRepository extends JpaRepository<Product, UUID> {
	boolean existsByName(String name);
}
