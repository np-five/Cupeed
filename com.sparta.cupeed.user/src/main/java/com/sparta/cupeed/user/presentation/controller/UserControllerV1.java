package com.sparta.cupeed.user.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.cupeed.user.application.service.UserServiceV1;
import com.sparta.cupeed.user.infrastructure.security.auth.UserDetailsImpl;
import com.sparta.cupeed.user.presentation.advice.ApiResponse;
import com.sparta.cupeed.user.presentation.dto.request.UserUpdateStatusRequestDtoV1;
import com.sparta.cupeed.user.presentation.dto.response.UserGetMyUserResponseDtoV1;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserControllerV1 {

	private final UserServiceV1 userServiceV1;

	@Operation(summary = "[MASTER/HUB] 사용자 가입 상태 변경", description = "사용자 가입 상태를 변경하는 API입니다. MASTER와 HUB 유저만 호출 가능합니다.")
	@PreAuthorize("hasAnyAuthority('ROLE_MASTER', 'ROLE_HUB')")
	@PatchMapping("/status")
	public ResponseEntity<ApiResponse<Void>> updateUserStatus(
		@RequestBody UserUpdateStatusRequestDtoV1 userUpdateStatusRequestDtoV1
	) {
		userServiceV1.updateUserStatus(userUpdateStatusRequestDtoV1);

		return ResponseEntity.ok(ApiResponse.success("사용자 가입 상태를 성공적으로 변경했습니다."));
	}

	@Operation(summary = "내 정보 조회", description = "내 정보를 조회하는 api")
	@GetMapping("/me")
	public ResponseEntity<ApiResponse<UserGetMyUserResponseDtoV1>> getMyUserInfo(
		@AuthenticationPrincipal UserDetailsImpl userDetails) {
		return ResponseEntity.ok(
			ApiResponse.success("내 정보 조회에 성공했습니다.", userServiceV1.getMyUserInfo(userDetails.getId())));
	}
}
