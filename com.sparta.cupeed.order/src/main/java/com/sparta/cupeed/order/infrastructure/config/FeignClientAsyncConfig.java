package com.sparta.cupeed.order.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestInterceptor;
import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class FeignClientAsyncConfig {
	@Bean
	public RequestInterceptor asyncRequestInterceptor() {
		return template -> {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth != null && auth.getCredentials() instanceof String token) {
				template.header("Authorization", "Bearer " + token);
			}
		};
	}
}
