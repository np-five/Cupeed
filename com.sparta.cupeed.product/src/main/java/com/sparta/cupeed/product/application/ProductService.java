package com.sparta.cupeed.product.application;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.cupeed.product.application.command.ProductCreateCommand;
import com.sparta.cupeed.product.domain.entity.Product;
import com.sparta.cupeed.product.domain.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository; // Domain Repository 의존

	@Transactional // Application Layer에서 트랜잭션 경계를 설정
	public UUID createProduct(ProductCreateCommand command) {

		// 1. 도메인 객체 생성 (Domain Entity의 정적 팩토리 메서드 호출)
		Product product = Product.create(
			command.companyId(),
			command.hubId(),
			command.name(),
			command.category(),
			command.description(),
			command.unitPrice(),
			command.initialQuantity(),
			command.createdBy()
		);

		// 2. 저장 (Domain Repository 인터페이스 호출)
		productRepository.save(product);

		// 3. 결과 반환
		return product.getId();
	}
}