package com.sparta.cupeed.user.auth.infrastructure.jpa.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.cupeed.user.auth.domain.model.User;
import com.sparta.cupeed.user.auth.domain.model.UserCompany;
import com.sparta.cupeed.user.auth.domain.repository.UserRepository;
import com.sparta.cupeed.user.auth.infrastructure.jpa.entity.UserCompanyEntity;
import com.sparta.cupeed.user.auth.infrastructure.jpa.entity.UserEntity;
import com.sparta.cupeed.user.auth.infrastructure.jpa.mapper.UserMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserRepositoryImpl implements UserRepository {

	private final UserJpaRepository userJpaRepository;
	private final UserMapper userMapper;

	@Override
	public Optional<User> findByUserId(String userId) {
		return userJpaRepository.findByUserId(userId).map(userMapper::toDomain);
	}

	@Override
	public Optional<User> findBySlackId(String slackId) {
		return userJpaRepository.findBySlackId(slackId).map(userMapper::toDomain);
	}

	@Override
	public void save(User user, UserCompany newUserCompany) {
		UserEntity userEntity = userMapper.toEntity(user);
		UserCompanyEntity userCompanyEntity = userMapper.toEntity(newUserCompany);

		userEntity.attachUserCompany(userCompanyEntity);

		userJpaRepository.save(userEntity);
	}
}
