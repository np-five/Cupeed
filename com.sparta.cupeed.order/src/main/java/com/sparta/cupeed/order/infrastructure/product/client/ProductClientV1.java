package com.sparta.cupeed.order.infrastructure.product.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.sparta.cupeed.order.infrastructure.product.dto.request.ProductStockDecreaseRequestDtoV1;

@FeignClient(name = "product")
public interface ProductClientV1 {
	// @PostMapping("/v1/products/{productId}/decrease-stock")
	// void decreaseStock(@PathVariable UUID productId, @RequestParam Long quantity);

	@PostMapping("/v1/products/decrease-stock")
	void decreaseStock(@RequestBody ProductStockDecreaseRequestDtoV1 requestDto);


	@PostMapping("/v1/products/{productId}/restore-stock")
	void restoreStock(@PathVariable UUID productId, @RequestParam Long quantity);
}
