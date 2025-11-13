package com.sparta.cupeed.user.presentation.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.cupeed.user.application.service.UserServiceV1;
import com.sparta.cupeed.user.presentation.dto.response.InternalUserResponseDtoV1;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/internal/v1/users")
@RequiredArgsConstructor
public class UserInternalControllerV1 {

	private final UserServiceV1 userService;

	@GetMapping("/{userId}")
	public InternalUserResponseDtoV1 getInternalCompanyIdByUserId(@PathVariable UUID userId) {
		return userService.getInternalUserByUserId(userId);
	}
}