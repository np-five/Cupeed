package com.sparta.cupeed.gateway.infrastructure.security.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sparta.cupeed.gateway.infrastructure.security.jwt.JwtProperties;
import com.sparta.cupeed.gateway.presentation.advice.GatewayError;
import com.sparta.cupeed.gateway.presentation.advice.GatewayException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j(topic = "AccessTokenValidationFilter")
@Component
@RequiredArgsConstructor
public class AccessTokenValidationFilter implements GlobalFilter, Ordered {

	private final JwtProperties jwtProperties;

	private static ServerWebExchange removeAuthorizationHeaderFromExchange(ServerWebExchange exchange) {
		ServerHttpRequest request = exchange.getRequest()
			.mutate()
			.headers(httpHeaders -> httpHeaders.remove(HttpHeaders.AUTHORIZATION))
			.build();

		return exchange.mutate()
			.request(request)
			.build();
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		String authorizedHeader = exchange.getRequest().getHeaders().getFirst(jwtProperties.getAccessHeaderName());

		log.info("token: {}", authorizedHeader);

		if (!StringUtils.hasText(authorizedHeader)) {
			return chain.filter(removeAuthorizationHeaderFromExchange(exchange));
		}

		String accessJwt = resolveAuthorizedToken(authorizedHeader);

		// signing 확인 및 유효성 검증
		DecodedJWT decodedJwt;
		try {
			decodedJwt = JWT.require(Algorithm.HMAC512(jwtProperties.getSecret()))
				.build()
				.verify(accessJwt);
		} catch (JWTVerificationException e) {
			throw new GatewayException(GatewayError.GATEWAY_TOKEN_INVALID);
		}

		// id 값 있는지 확인 <- 이후의 인가 작업을 위해서
		String id = decodedJwt.getClaim("id").asString();
		if (!StringUtils.hasText(id)) {
			throw new GatewayException(GatewayError.GATEWAY_TOKEN_INVALID);
		}

		// // TODO: Redis를 사용해서 유저 단위 컷오프 시각(invalidateBefore) 유효성 검증

		return chain.filter(exchange);
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}

	private String resolveAuthorizedToken(String authorizedHeader) {
		if (authorizedHeader.regionMatches(
			true,
			0,
			jwtProperties.getHeaderPrefix(),
			0,
			jwtProperties.getHeaderPrefix().length())
		) {
			return authorizedHeader.substring(jwtProperties.getHeaderPrefix().length()).trim();
		}

		return authorizedHeader.trim();
	}
}
