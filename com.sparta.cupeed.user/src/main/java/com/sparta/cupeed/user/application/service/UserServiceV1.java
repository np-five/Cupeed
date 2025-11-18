package com.sparta.cupeed.user.application.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.cupeed.user.domain.model.User;
import com.sparta.cupeed.user.domain.model.UserCompany;
import com.sparta.cupeed.user.domain.model.UserDelivery;
import com.sparta.cupeed.user.domain.repository.UserRepository;
import com.sparta.cupeed.user.domain.vo.UserRoleEnum;
import com.sparta.cupeed.user.domain.vo.UserStatusEnum;
import com.sparta.cupeed.user.infrastructure.hub.client.HubClientV1;
import com.sparta.cupeed.user.infrastructure.hub.dto.response.HubGetResponseDtoV1;
import com.sparta.cupeed.user.presentation.advice.UserError;
import com.sparta.cupeed.user.presentation.advice.UserException;
import com.sparta.cupeed.user.presentation.dto.request.UserUpdateStatusRequestDtoV1;
import com.sparta.cupeed.user.presentation.dto.response.InternalUserResponseDtoV1;
import com.sparta.cupeed.user.presentation.dto.response.UserGetMyUserResponseDtoV1;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceV1 {

	private final UserRepository userRepository;

	private final HubClientV1 hubClientV1;

	@Transactional
	public void updateUserStatus(UserUpdateStatusRequestDtoV1 userUpdateStatusRequestDtoV1) {
		User user = userRepository.findByIdOrElseThrow(userUpdateStatusRequestDtoV1.id());

		if (user.getRole() == UserRoleEnum.MASTER) {
			throw new UserException(UserError.USER_UPDATE_MASTER_FORBIDDEN);
		}

		if (user.getStatus() != UserStatusEnum.PENDING) {
			throw new UserException(UserError.USER_STATUS_NOT_PENDING);
		}

		userRepository.saveStatus(user, userUpdateStatusRequestDtoV1.status());
	}

	public InternalUserResponseDtoV1 getInternalUserByUserId(UUID userId) {
		User user = userRepository.findByIdOrElseThrow(userId);

		return InternalUserResponseDtoV1.builder()
			.user(
				InternalUserResponseDtoV1.UserDto.builder()
					.id(user.getId())
					.companyId(user.getCompanyId())
					.build()
			)
			.build();
	}

	@Transactional
	public UserGetMyUserResponseDtoV1 getMyUserInfo(UUID id) {
		User user = userRepository.findByIdOrElseThrow(id);
		UserCompany userCompany = userRepository.findCompanyByUserIdOrElseGetNull(id);
		UserDelivery userDelivery = userRepository.findDeliveryByUserIdOrElseGetNull(id);

		HubGetResponseDtoV1 hub = hubClientV1.getHubById(user.getHubId());

		return UserGetMyUserResponseDtoV1.of(user, userCompany, userDelivery, hub.getName());
	}
}
