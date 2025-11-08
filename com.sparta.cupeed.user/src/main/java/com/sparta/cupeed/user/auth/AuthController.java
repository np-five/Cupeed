package com.sparta.cupeed.user.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/v1/auth")
@Tag(name = "인증 API", description = "인증 관련 기능 API입니다.")
public class AuthController {

	@Operation(summary = "회원 가입", description = "회원 가입 api입니다.")
	@PostMapping("/sign-up")
	public ResponseEntity<AuthSignUpResponseDto> getSignUp(@RequestBody AuthSignUpCommand authSignUpCommand) {
		return ResponseEntity.ok(new AuthSignUpResponseDto("sign up test token"));
	}
}
