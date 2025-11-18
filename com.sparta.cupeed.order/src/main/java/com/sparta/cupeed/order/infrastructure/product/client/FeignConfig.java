package com.sparta.cupeed.order.infrastructure.product.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestInterceptor;

@Configuration
public class FeignConfig {
	@Bean
	public RequestInterceptor authInterceptor() {
		return template -> {
			String token = RequestContextHolder.getRequestAttributes() instanceof ServletRequestAttributes attrs
				? attrs.getRequest().getHeader("Authorization")
				: null;

			if (token != null) template.header("Authorization", token);
		};
	}
}
