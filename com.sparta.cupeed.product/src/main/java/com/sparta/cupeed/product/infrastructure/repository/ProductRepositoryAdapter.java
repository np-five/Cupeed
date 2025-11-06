// // ProductRepositoryImpl.java
// package com.sparta.cupeed.product.infrastructure.repository;
//
// import java.util.Optional;
// import java.util.UUID;
// import org.springframework.stereotype.Repository;
// import com.sparta.cupeed.product.domain.entity.Product;
// import com.sparta.cupeed.product.domain.repository.ProductRepository;
// import lombok.RequiredArgsConstructor;
//
// @Repository
// @RequiredArgsConstructor
// public class ProductRepositoryAdapter implements ProductRepository {
// 	private final ProductJpaRepository jpaRepository;
//
// 	@Override
// 	public void save(Product product) { jpaRepository.save(product); }
// 	@Override
// 	public Optional<Product> findById(UUID id) { return jpaRepository.findById(id); }
// 	@Override
// 	public boolean existsByName(String name) { return jpaRepository.existsByName(name); }
// }
