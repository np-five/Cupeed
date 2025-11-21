package com.sparta.cupeed.user.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.cupeed.user.application.service.AuthServiceV1;
import com.sparta.cupeed.user.presentation.advice.ApiResponse;
import com.sparta.cupeed.user.presentation.dto.request.AuthLogInRequestDtoV1;
import com.sparta.cupeed.user.presentation.dto.request.AuthSignUpRequestDtoV1;
import com.sparta.cupeed.user.presentation.dto.response.AuthLogInResponseDtoV1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
@Tag(name = "인증 API", description = "인증 관련 기능 API입니다.")
public class AuthControllerV1 {

	private final AuthServiceV1 authServiceV1;

	@Operation(summary = "회원 가입", description = "회원 가입 api입니다.")
	@PostMapping("/sign-up")
	public ResponseEntity<ApiResponse<Void>> signUp(@RequestBody @Valid AuthSignUpRequestDtoV1 authSignUpRequestDtoV1) {
		authServiceV1.signUp(authSignUpRequestDtoV1);
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("회원가입에 성공했습니다."));
	}

	@Operation(summary = "로그인", description = "로그인 api입니다.")
	@PostMapping("/sign-in")
	public ResponseEntity<ApiResponse<AuthLogInResponseDtoV1>> signIn(
		@RequestBody @Valid AuthLogInRequestDtoV1 authLogInRequestDtoV1
	) {
		AuthLogInResponseDtoV1 authLogInResponseDtoV1 = authServiceV1.signIn(authLogInRequestDtoV1);
		return ResponseEntity.ok(ApiResponse.success("로그인에 성공했습니다.", authLogInResponseDtoV1));
	}
}
