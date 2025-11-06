package com.sparta.cupeed.product;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1")
public class ProductController {

	@GetMapping("/products")
	public List<Product> getProducts(){
		List<Product> products = new ArrayList<>();
		products.add(new Product("애슐리볶음밥", "애슐리레시피로 만든 볶음밥입니다."));
		products.add(new Product("신라면 툼바", "신라면의 히트작! 신라면 툼바!"));
		return products;
	}
}
