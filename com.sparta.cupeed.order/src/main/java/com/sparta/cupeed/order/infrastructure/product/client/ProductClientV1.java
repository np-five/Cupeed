package com.sparta.cupeed.order.infrastructure.product.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.sparta.cupeed.order.infrastructure.product.dto.request.ProductStockRequestDtoV1;

@FeignClient(name = "product")
public interface ProductClientV1 {
	@PostMapping("/v1/products/decrease-stock")
	void decreaseStock(@RequestBody ProductStockRequestDtoV1 requestDto);

	@PostMapping("/v1/products/{productId}/restore-stock")
	void restoreStock(@RequestBody ProductStockRequestDtoV1 requestDto);
}
