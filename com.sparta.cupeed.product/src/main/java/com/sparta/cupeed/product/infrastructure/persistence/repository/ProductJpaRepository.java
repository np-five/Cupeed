package com.sparta.cupeed.product.infrastructure.persistence.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sparta.cupeed.product.infrastructure.persistence.entity.ProductEntity;

@Repository
public interface ProductJpaRepository extends JpaRepository<ProductEntity, UUID> {
	boolean existsByName(String name);
}
