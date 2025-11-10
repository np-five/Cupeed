package com.sparta.cupeed.user.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.cupeed.user.auth.domain.model.User;
import com.sparta.cupeed.user.auth.infrastructure.jpa.mapper.UserMapper;
import com.sparta.cupeed.user.auth.infrastructure.security.auth.UserDetailsImpl;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
public class UserController {

	private final UserMapper userMapper;

	@Operation(summary = "인증 테스트", description = "헤더에 토큰 넣고 인증 테스트를 할 수 있습니다.")
	@GetMapping("/auth-test")
	public ResponseEntity<?> testEndpoint(@AuthenticationPrincipal UserDetailsImpl userDetails) {
		User user = userMapper.toDomain(userDetails.getUserEntity());
		return ResponseEntity.ok(user.toString());
	}
}
