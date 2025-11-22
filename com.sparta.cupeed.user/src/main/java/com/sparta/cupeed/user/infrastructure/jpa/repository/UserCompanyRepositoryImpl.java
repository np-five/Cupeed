package com.sparta.cupeed.user.infrastructure.jpa.repository;

import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.sparta.cupeed.user.domain.model.UserCompany;
import com.sparta.cupeed.user.domain.repository.UserCompanyRepository;
import com.sparta.cupeed.user.infrastructure.jpa.entity.UserCompanyEntity;
import com.sparta.cupeed.user.infrastructure.jpa.mapper.UserMapper;
import com.sparta.cupeed.user.presentation.advice.UserError;
import com.sparta.cupeed.user.presentation.advice.UserException;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserCompanyRepositoryImpl implements UserCompanyRepository {
	private final UserCompanyJpaRepository userCompanyJpaRepository;
	private final UserMapper userMapper;

	@Override
	public UserCompany findUserCompanyByUserId(UUID id) {
		UserCompanyEntity entity = userCompanyJpaRepository.findById(id).orElseThrow(() ->
			new UserException(UserError.AUTH_USER_NOT_FOUND));
		return userMapper.toDomain(entity);
	}
}
