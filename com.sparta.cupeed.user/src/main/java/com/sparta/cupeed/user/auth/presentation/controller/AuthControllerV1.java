package com.sparta.cupeed.user.auth.presentation.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.cupeed.user.auth.application.service.AuthServiceV1;
import com.sparta.cupeed.user.auth.infrastructure.jpa.entity.RoleEnum;
import com.sparta.cupeed.user.auth.infrastructure.security.jwt.JwtUtil;
import com.sparta.cupeed.user.auth.presentation.dto.request.AuthSignUpRequestDtoV1;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
@Tag(name = "인증 API", description = "인증 관련 기능 API입니다.")
public class AuthControllerV1 {

	private final JwtUtil jwtUtil;

	private final AuthServiceV1 authServiceV1;

	@Operation(summary = "회원 가입", description = "회원 가입 api입니다.")
	@PostMapping("/sign-up")
	public ResponseEntity<String> signUp(
		@RequestBody AuthSignUpRequestDtoV1 authSignUpRequestDtoV1
	) {
		authServiceV1.signUp(authSignUpRequestDtoV1);
		return ResponseEntity.status(HttpStatus.CREATED).body("success");
	}

	@GetMapping("/create-jwt")
	public String createJwt(HttpServletResponse res) {
		// Jwt 생성
		String token = jwtUtil.createToken(UUID.randomUUID(), RoleEnum.MASTER);

		// Jwt 쿠키 저장
		jwtUtil.addJwtToCookie(token, res);

		return "createJwt : " + token;
	}

	@GetMapping("/get-jwt")
	public String getJwt(@CookieValue(JwtUtil.AUTHORIZATION_HEADER) String tokenValue) {
		// JWT 토큰 substring
		String token = jwtUtil.substringToken(tokenValue);

		// 토큰 검증
		if (!jwtUtil.validateToken(token)) {
			throw new IllegalArgumentException("Token Error");
		}

		// 토큰에서 사용자 정보 가져오기
		Claims info = jwtUtil.getUserInfoFromToken(token);
		// 사용자 username
		String username = info.getSubject();
		System.out.println("uuid = " + username);
		// 사용자 권한
		String authority = (String)info.get(JwtUtil.AUTHORIZATION_KEY);
		System.out.println("role = " + authority);

		return "getJwt : " + username + ", " + authority;
	}
}
