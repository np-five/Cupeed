package com.sparta.cupeed.product.infrastructure.security.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Getter
@Component
public class JwtProperties {

	private final String accessHeaderName;
	private final String headerPrefix;
	private final String bearerSchema;

	public JwtProperties(
		@Value("${cupeed.jwt.access-header-name}") String accessHeaderName,
		@Value("${cupeed.jwt.header-prefix}") String headerPrefix,
		@Value("${cupeed.jwt.swagger-bearer-schema}") String bearerSchema
	) {
		this.accessHeaderName = accessHeaderName;
		this.headerPrefix = headerPrefix;
		this.bearerSchema = bearerSchema;
	}
}
