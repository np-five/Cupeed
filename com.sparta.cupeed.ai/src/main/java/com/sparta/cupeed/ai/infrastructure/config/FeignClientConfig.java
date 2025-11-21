package com.sparta.cupeed.ai.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class FeignClientConfig {

	@Bean
	public RequestInterceptor requestInterceptor() {
		return template -> {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth != null && auth.getCredentials() instanceof String token) {
				template.header("Authorization", "Bearer " + token);
			}
		};
	}

	// 아래 코드는 동기에서만 동작
	// @Bean
	// public RequestInterceptor requestInterceptor() {
	// 	return requestTemplate -> {
	// 		ServletRequestAttributes attributes =
	// 			(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
	//
	// 		if (attributes != null) {
	// 			HttpServletRequest request = attributes.getRequest();
	// 			String token = request.getHeader("Authorization");
	//
	// 			if (token != null) {
	// 				requestTemplate.header("Authorization", token);
	// 			}
	// 		}
	// 	};
	// }
}
