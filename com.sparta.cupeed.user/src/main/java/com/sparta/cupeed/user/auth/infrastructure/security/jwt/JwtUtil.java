package com.sparta.cupeed.user.auth.infrastructure.security.jwt;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.sparta.cupeed.user.auth.domain.vo.UserRoleEnum;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "JwtUtil")
@Component
@RequiredArgsConstructor
public class JwtUtil {

	private final JwtProperties jwtProperties;
	private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;

	private Key key;

	// key 생성
	@PostConstruct        // 다중 호출 방지
	public void init() {
		byte[] bytes = Base64.getDecoder().decode(jwtProperties.getSecret());
		key = Keys.hmacShaKeyFor(bytes);
	}

	// JWT 토큰 생성
	public String createToken(String userId, UserRoleEnum role) {
		Date date = new Date();

		return jwtProperties.getHeaderPrefix() + Jwts.builder()
			.setSubject(userId) // 사용자 식별자값(아이디)
			.claim("role", role) // 사용자 권한
			.setExpiration(new Date(date.getTime() + jwtProperties.getExpirationTime())) // 만료 시간
			.setIssuedAt(date) // 발급일
			.signWith(key, signatureAlgorithm) // 암호화 알고리즘
			.compact();
	}

	// header 에서 JWT 가져오기
	public String getJwtTokenFromHeader(HttpServletRequest request) {
		String bearerToken = request.getHeader(jwtProperties.getAccessHeaderName());

		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(jwtProperties.getHeaderPrefix())) {
			return bearerToken.substring(jwtProperties.getHeaderPrefix().length());
		}

		return null;
	}

	// JWT 검증
	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (SecurityException | MalformedJwtException | SignatureException e) {
			log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
		} catch (ExpiredJwtException e) {
			log.error("Expired JWT token, 만료된 JWT token 입니다.");
		} catch (UnsupportedJwtException e) {
			log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
		} catch (IllegalArgumentException e) {
			log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
		}

		return false;
	}

	// JWT에서 사용자 정보 추출
	public Claims getUserInfoFromToken(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
	}
}


