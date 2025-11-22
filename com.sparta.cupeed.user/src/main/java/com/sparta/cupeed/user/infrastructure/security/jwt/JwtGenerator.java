package com.sparta.cupeed.user.infrastructure.security.jwt;

import java.time.Instant;

import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.sparta.cupeed.user.domain.model.User;
import com.sparta.cupeed.user.domain.model.UserCompany;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtGenerator {

	private final JwtProperties jwtProperties;

	public String createToken(User user, UserCompany userCompany) {
		Instant now = Instant.now();

		String companyName = userCompany != null ? userCompany.getCompanyName() : null;

		return jwtProperties.getHeaderPrefix() + JWT.create()
			.withSubject(user.getUserId())
			.withClaim("id", user.getId().toString())
			.withClaim("role", user.getRole().getAuthority())
			.withClaim("companyId", String.valueOf(user.getCompanyId()))
			.withClaim("companyName", companyName)
			.withClaim("hubId", String.valueOf(user.getHubId()))
			.withClaim("slackId", String.valueOf(user.getSlackId()))
			.withIssuedAt(now)
			.withExpiresAt(now.plusMillis(jwtProperties.getExpirationTime()))
			.sign(Algorithm.HMAC512(jwtProperties.getSecret()));
	}

	// JWT 토큰 생성
	// public String createToken(User user) {
	// 	Instant now = Instant.now();
	//
	// 	return jwtProperties.getHeaderPrefix() + JWT.create()
	// 		.withSubject(user.getUserId()) // 사용자 식별자값(아이디)
	// 		.withClaim("id", user.getId().toString())
	// 		.withClaim("role", user.getRole().getAuthority()) // 사용자 권한
	// 		.withClaim("companyId", String.valueOf(user.getCompanyId()))
	// 		.withClaim("hubId", String.valueOf(user.getHubId()))
	// 		.withClaim("slackId", String.valueOf(user.getSlackId()))
	// 		.withIssuedAt(now) // 발급일
	// 		.withExpiresAt(now.plusMillis(jwtProperties.getExpirationTime())) // 만료 시간
	// 		.sign(Algorithm.HMAC512(jwtProperties.getSecret())); // 암호화 알고리즘
	// }
}


