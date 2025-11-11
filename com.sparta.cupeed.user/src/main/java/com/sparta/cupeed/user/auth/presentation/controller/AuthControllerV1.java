package com.sparta.cupeed.user.auth.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.cupeed.user.auth.application.service.AuthServiceV1;
import com.sparta.cupeed.user.auth.presentation.dto.request.AuthLogInRequestDtoV1;
import com.sparta.cupeed.user.auth.presentation.dto.request.AuthSignUpRequestDtoV1;
import com.sparta.cupeed.user.auth.presentation.dto.response.AuthLogInResponseDtoV1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
@Tag(name = "인증 API", description = "인증 관련 기능 API입니다.")
public class AuthControllerV1 {

	private final AuthServiceV1 authServiceV1;

	@Operation(summary = "회원 가입", description = "회원 가입 api입니다.")
	@PostMapping("/sign-up")
	public ResponseEntity<String> signUp(@RequestBody AuthSignUpRequestDtoV1 authSignUpRequestDtoV1) {
		authServiceV1.signUp(authSignUpRequestDtoV1);
		return ResponseEntity.status(HttpStatus.CREATED).body("success");
	}

	@Operation(summary = "로그인", description = "로그인 api입니다.")
	@PostMapping("/log-in")
	public ResponseEntity<AuthLogInResponseDtoV1> logIn(@RequestBody AuthLogInRequestDtoV1 authLogInRequestDtoV1) {
		return ResponseEntity.ok(authServiceV1.logIn(authLogInRequestDtoV1));
	}
}
