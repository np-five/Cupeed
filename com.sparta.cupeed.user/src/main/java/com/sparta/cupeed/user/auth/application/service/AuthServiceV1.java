package com.sparta.cupeed.user.auth.application.service;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.cupeed.user.auth.domain.model.User;
import com.sparta.cupeed.user.auth.domain.model.UserCompany;
import com.sparta.cupeed.user.auth.domain.repository.UserRepository;
import com.sparta.cupeed.user.auth.domain.vo.UserRoleEnum;
import com.sparta.cupeed.user.auth.domain.vo.UserStatusEnum;
import com.sparta.cupeed.user.auth.infrastructure.security.jwt.JwtUtil;
import com.sparta.cupeed.user.auth.presentation.advice.AuthError;
import com.sparta.cupeed.user.auth.presentation.advice.AuthException;
import com.sparta.cupeed.user.auth.presentation.dto.request.AuthLogInRequestDtoV1;
import com.sparta.cupeed.user.auth.presentation.dto.request.AuthSignUpRequestDtoV1;
import com.sparta.cupeed.user.auth.presentation.dto.response.AuthLogInResponseDtoV1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceV1 {

	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	private final UserRepository userRepository;

	@Transactional
	public void signUp(AuthSignUpRequestDtoV1 authSignUpRequestDtoV1) {
		// 아이디 중복 확인
		User user = userRepository.findByUserId(authSignUpRequestDtoV1.userId()).orElse(null);
		if (user != null) {
			throw new AuthException(AuthError.AUTH_USER_ID_ALREADY_EXISTS);
		}

		// 슬랙 아이디 중복 확인
		user = userRepository.findBySlackId(authSignUpRequestDtoV1.slackId()).orElse(null);
		if (user != null) {
			throw new AuthException(AuthError.AUTH_USER_ID_ALREADY_EXISTS);
		}

		User newUser = User.builder()
			.userId(authSignUpRequestDtoV1.userId())
			.password(passwordEncoder.encode(authSignUpRequestDtoV1.password()))
			.slackId(authSignUpRequestDtoV1.slackId())
			.role(UserRoleEnum.COMPANY)
			.status(UserStatusEnum.PENDING)
			//TODO: created auditing 처리
			.createdAt(LocalDateTime.now())
			.createdBy("system")
			.build();

		UserCompany newUserCompany = UserCompany.builder()
			.user(newUser)
			.companyName(authSignUpRequestDtoV1.companyName())
			.businessNo(authSignUpRequestDtoV1.businessNo())
			//TODO: created auditing 처리
			.createdAt(LocalDateTime.now())
			.createdBy("system")
			.build();

		// TODO: 로그 삭제
		log.info("user: {}", userRepository.save(newUser, newUserCompany));
	}

	public AuthLogInResponseDtoV1 logIn(AuthLogInRequestDtoV1 authLogInRequestDtoV1) {
		User user = userRepository.findByUserId(authLogInRequestDtoV1.userId()).orElseThrow(() ->
			new AuthException(AuthError.AUTH_USER_NOT_FOUND));

		if (!passwordEncoder.matches(authLogInRequestDtoV1.password(), user.getPassword())) {
			throw new AuthException(AuthError.AUTH_INVALID_PASSWORD);
		}

		return new AuthLogInResponseDtoV1("Bearer", jwtUtil.createToken(user.getUserId(), user.getRole()));
	}
}
