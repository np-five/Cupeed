package com.sparta.cupeed.user.infrastructure.security.jwt;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.sparta.cupeed.user.domain.vo.UserRoleEnum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "JwtUtil")
@Component
@RequiredArgsConstructor
public class JwtUtil {
	// TODO: token에 companyId 담아주세요. <- 초희님 요청

	private final JwtProperties jwtProperties;

	// JWT 토큰 생성
	public String createToken(UUID id, String userId, UserRoleEnum role) {
		Instant now = Instant.now();

		return jwtProperties.getHeaderPrefix() + JWT.create()
			.withSubject(userId) // 사용자 식별자값(아이디)
			.withClaim("id", id.toString())
			.withClaim("role", role.getAuthority()) // 사용자 권한
			.withIssuedAt(now) // 발급일
			.withExpiresAt(now.plusMillis(jwtProperties.getExpirationTime())) // 만료 시간
			.sign(Algorithm.HMAC512(jwtProperties.getSecret())); // 암호화 알고리즘
	}
}


