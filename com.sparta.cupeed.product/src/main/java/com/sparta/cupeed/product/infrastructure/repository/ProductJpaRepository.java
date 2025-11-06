package com.sparta.cupeed.product.infrastructure.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sparta.cupeed.product.domain.entity.Product;

@Repository
public interface ProductJpaRepository extends JpaRepository<Product, UUID> {
	boolean existsByName(String name);
}
