package com.sparta.cupeed.gateway.infrastructure.security.jwt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Getter
@Component
public class JwtProperties {

	private final String secret;
	private final String accessHeaderName;
	private final String headerPrefix;

	public JwtProperties(
		@Value("${cupeed.jwt.secret}") String secret,
		@Value("${cupeed.jwt.access-header-name}") String accessHeaderName,
		@Value("${cupeed.jwt.header-prefix}") String headerPrefix
	) {
		this.secret = secret;
		this.accessHeaderName = accessHeaderName;
		this.headerPrefix = headerPrefix;
	}
}
