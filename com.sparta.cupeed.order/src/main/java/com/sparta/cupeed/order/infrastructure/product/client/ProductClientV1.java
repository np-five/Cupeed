package com.sparta.cupeed.order.infrastructure.product.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.sparta.cupeed.order.infrastructure.config.FeignClientConfig;
import com.sparta.cupeed.order.infrastructure.product.dto.request.ProductStockRequestDtoV1;
import com.sparta.cupeed.order.infrastructure.product.dto.response.ProductGetResponseDtoV1;

@FeignClient(name = "product", configuration = FeignClientConfig.class)
public interface ProductClientV1 {
	@PostMapping("/internal/v1/products/decrease-stock")
	void decreaseStock(@RequestBody ProductStockRequestDtoV1 requestDto);

	@PostMapping("/internal/v1/products/restore-stock")
	void restoreStock(@RequestBody ProductStockRequestDtoV1 requestDto);

	@GetMapping("/internal/v1/products/{productId}")
	ProductGetResponseDtoV1 getProduct(@PathVariable("productId") UUID productId);
}
