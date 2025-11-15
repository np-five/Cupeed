package com.sparta.cupeed.product.infrastructure.persistence.repository;

import com.sparta.cupeed.product.domain.model.Product;
import com.sparta.cupeed.product.domain.repository.ProductRepository;
import com.sparta.cupeed.product.infrastructure.persistence.entity.ProductEntity;
import com.sparta.cupeed.product.infrastructure.persistence.mapper.ProductMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductRepositoryImpl implements ProductRepository {

	private final ProductJpaRepository productJpaRepository;
	private final ProductMapper productMapper;

	@Override
	@Transactional
	public Product save(Product product) {
		ProductEntity entity;
		if (product.getId() != null) {
			entity = productJpaRepository.findById(product.getId())
				.orElseGet(() -> productMapper.toEntity(product));
			productMapper.applyDomain(product, entity);
		} else {
			entity = productMapper.toEntity(product);
		}
		ProductEntity saved = productJpaRepository.save(entity);
		return productMapper.toDomain(saved);
	}

	@Override
	public Optional<Product> findById(UUID id) {
		return productJpaRepository.findById(id)
			.map(productMapper::toDomain);
	}

	@Override
	public boolean existsByName(String name) {
		return productJpaRepository.existsByName(name);
	}

	public Page<Product> findAll(Pageable pageable) {
		return productJpaRepository.findAll(pageable)
			.map(productMapper::toDomain);
	}

	@Override
	public List<Product> findAllById(List<UUID> productIds) {
		// JpaRepository에서 엔티티를 한 번에 조회 후 도메인으로 매핑
		List<ProductEntity> entities = productJpaRepository.findAllById(productIds);
		return entities.stream()
			.map(productMapper::toDomain)
			.toList();
	}

	@Override
	@Transactional
	public void saveAll(List<Product> updatedProducts) {
		// 도메인을 엔티티로 변환하고 일괄 저장
		List<ProductEntity> entities = updatedProducts.stream()
			.map(productMapper::toEntity)
			.toList();

		productJpaRepository.saveAll(entities);
	}
}
