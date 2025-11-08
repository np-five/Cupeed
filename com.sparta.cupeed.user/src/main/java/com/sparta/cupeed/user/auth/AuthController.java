package com.sparta.cupeed.user.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Operation(summary = "회원 가입", description = "회원 가입 api입니다.")
	@PostMapping("/sign-up")
	public ResponseEntity<AuthSignUpResponseDto> getSignUp(@RequestBody AuthSignUpCommand authSignUpCommand) {
		return ResponseEntity.ok(new AuthSignUpResponseDto("sign up test token"));
	}
}
