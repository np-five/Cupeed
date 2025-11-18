package com.sparta.cupeed.user.application.service;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.cupeed.user.domain.model.User;
import com.sparta.cupeed.user.domain.model.UserCompany;
import com.sparta.cupeed.user.domain.model.UserDelivery;
import com.sparta.cupeed.user.domain.repository.UserRepository;
import com.sparta.cupeed.user.domain.vo.UserDeliveryTypeEnum;
import com.sparta.cupeed.user.domain.vo.UserRoleEnum;
import com.sparta.cupeed.user.domain.vo.UserStatusEnum;
import com.sparta.cupeed.user.infrastructure.company.client.CompanyClientV1;
import com.sparta.cupeed.user.infrastructure.hub.client.HubClientV1;
import com.sparta.cupeed.user.infrastructure.hub.dto.response.HubGetResponseDtoV1;
import com.sparta.cupeed.user.infrastructure.security.jwt.JwtGenerator;
import com.sparta.cupeed.user.presentation.advice.UserError;
import com.sparta.cupeed.user.presentation.advice.UserException;
import com.sparta.cupeed.user.presentation.dto.request.AuthLogInRequestDtoV1;
import com.sparta.cupeed.user.presentation.dto.request.AuthSignUpRequestDtoV1;
import com.sparta.cupeed.user.presentation.dto.response.AuthLogInResponseDtoV1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceV1 {

	private final PasswordEncoder passwordEncoder;
	private final JwtGenerator jwtGenerator;

	private final UserRepository userRepository;

	private final HubClientV1 hubClientV1;
	private final CompanyClientV1 companyClientV1;

	@Transactional
	public void signUp(AuthSignUpRequestDtoV1 authSignUpRequestDtoV1) {
		// 아이디 중복 확인
		User user = userRepository.findByUserId(authSignUpRequestDtoV1.userId()).orElse(null);
		if (user != null) {
			throw new UserException(UserError.AUTH_USER_ID_ALREADY_EXISTS);
		}

		// 슬랙 아이디 중복 확인
		user = userRepository.findBySlackId(authSignUpRequestDtoV1.slackId()).orElse(null);
		if (user != null) {
			throw new UserException(UserError.AUTH_USER_ID_ALREADY_EXISTS);
		}

		// role 체크
		UserRoleEnum userRoleEnum;
		try {
			userRoleEnum = UserRoleEnum.valueOf(authSignUpRequestDtoV1.role());
		} catch (IllegalArgumentException e) {
			throw new UserException(UserError.AUTH_INVALID_ROLE);
		}

		User.UserBuilder userBuilder = User.builder()
			.userId(authSignUpRequestDtoV1.userId())
			.password(passwordEncoder.encode(authSignUpRequestDtoV1.password()))
			.slackId(authSignUpRequestDtoV1.slackId())
			.role(userRoleEnum)
			.status(UserStatusEnum.PENDING);

		// role에 따라 연관 데이터 생성
		User newUser;
		UserCompany newUserCompany;
		UserDelivery newUserDelivery;
		switch (userRoleEnum) {
			case MASTER -> {
				newUser = userBuilder.build();

				userRepository.save(newUser);
			}
			case HUB -> {
				// 허브 ID
				if (authSignUpRequestDtoV1.hubName() == null) {
					throw new UserException(UserError.AUTH_EMPTY_HUB_NAME);
				}

				HubGetResponseDtoV1 hubGetResponseDtoV1 = hubClientV1.getHubByName(authSignUpRequestDtoV1.hubName());
				log.info("HubGetResponseDtoV1: {}", hubGetResponseDtoV1.toString());

				newUser = userBuilder.hubId(hubGetResponseDtoV1.getId()).build();

				userRepository.save(newUser);
			}
			case COMPANY -> {
				// 업체 ID, 업체 이름, 사업자 등록 번호
				if (authSignUpRequestDtoV1.companyName() == null || authSignUpRequestDtoV1.businessNo() == null) {
					throw new UserException(UserError.AUTH_EMPTY_COMPANY_INFO);
				}

				UUID companyId = companyClientV1.getInternalCompanyByBusinessNo(authSignUpRequestDtoV1.businessNo());
				newUser = userBuilder.companyId(companyId).build();

				newUserCompany = UserCompany.builder()
					.user(newUser)
					.companyName(authSignUpRequestDtoV1.companyName())
					.businessNo(authSignUpRequestDtoV1.businessNo())
					.build();

				userRepository.save(newUser, newUserCompany);
			}
			case DELIVERY -> {
				// 허브 ID, 타입, 배송 순번
				if (authSignUpRequestDtoV1.hubName() == null) {
					throw new UserException(UserError.AUTH_EMPTY_HUB_NAME);
				}

				UserDeliveryTypeEnum deliveryType;
				try {
					deliveryType = UserDeliveryTypeEnum.valueOf(authSignUpRequestDtoV1.deliveryType());
				} catch (IllegalArgumentException e) {
					throw new UserException(UserError.AUTH_INVALID_DELIVERY_TYPE);
				}

				if (authSignUpRequestDtoV1.deliveryOrder() == null
					|| authSignUpRequestDtoV1.deliveryOrder() < 1
					|| authSignUpRequestDtoV1.deliveryOrder() > 10) {
					throw new UserException(UserError.AUTH_INVALID_DELIVERY_ORDER);
				}

				HubGetResponseDtoV1 hubGetResponseDtoV1 = hubClientV1.getHubByName(authSignUpRequestDtoV1.hubName());
				log.info("HubGetResponseDtoV1: {}", hubGetResponseDtoV1.toString());

				newUser = userBuilder.hubId(hubGetResponseDtoV1.getId()).build();

				newUserDelivery = UserDelivery.builder()
					.deliveryType(deliveryType)
					.deliveryOrder(authSignUpRequestDtoV1.deliveryOrder())
					.build();

				userRepository.save(newUser, newUserDelivery);
			}
		}
	}

	public AuthLogInResponseDtoV1 signIn(AuthLogInRequestDtoV1 authLogInRequestDtoV1) {
		User user = userRepository.findByUserIdOrElseThrow(authLogInRequestDtoV1.userId());

		if (!passwordEncoder.matches(authLogInRequestDtoV1.password(), user.getPassword())) {
			throw new UserException(UserError.AUTH_INVALID_PASSWORD);
		}

		return AuthLogInResponseDtoV1.of(jwtGenerator.createToken(user));
	}
}
