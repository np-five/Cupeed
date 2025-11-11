package com.sparta.cupeed.user.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.cupeed.user.domain.model.User;
import com.sparta.cupeed.user.domain.repository.UserRepository;
import com.sparta.cupeed.user.domain.vo.UserRoleEnum;
import com.sparta.cupeed.user.domain.vo.UserStatusEnum;
import com.sparta.cupeed.user.presentation.advice.AuthError;
import com.sparta.cupeed.user.presentation.advice.AuthException;
import com.sparta.cupeed.user.presentation.dto.request.UserUpdateStatusRequestDtoV1;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceV1 {

	private final UserRepository userRepository;

	@Transactional
	public void updateUserStatus(UserUpdateStatusRequestDtoV1 userUpdateStatusRequestDtoV1) {
		User user = userRepository.findByIdOrElseThrow(userUpdateStatusRequestDtoV1.id());

		if (user.getRole() == UserRoleEnum.MASTER) {
			throw new AuthException(AuthError.AUTH_UPDATE_MASTER_FORBIDDEN);
		}

		if (user.getStatus() != UserStatusEnum.PENDING) {
			throw new AuthException(AuthError.AUTH_STATUS_NOT_PENDING);
		}

		userRepository.saveStatus(user, userUpdateStatusRequestDtoV1.status());
	}
}
