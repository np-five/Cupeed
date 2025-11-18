package com.sparta.cupeed.user.infrastructure.jpa.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.cupeed.user.domain.model.User;
import com.sparta.cupeed.user.domain.model.UserCompany;
import com.sparta.cupeed.user.domain.model.UserDelivery;
import com.sparta.cupeed.user.domain.repository.UserRepository;
import com.sparta.cupeed.user.infrastructure.jpa.entity.StatusEnum;
import com.sparta.cupeed.user.infrastructure.jpa.entity.UserCompanyEntity;
import com.sparta.cupeed.user.infrastructure.jpa.entity.UserDeliveryEntity;
import com.sparta.cupeed.user.infrastructure.jpa.entity.UserEntity;
import com.sparta.cupeed.user.infrastructure.jpa.mapper.UserMapper;
import com.sparta.cupeed.user.presentation.advice.UserError;
import com.sparta.cupeed.user.presentation.advice.UserException;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserRepositoryImpl implements UserRepository {

	private final UserJpaRepository userJpaRepository;
	private final UserMapper userMapper;

	@Override
	public User findByIdOrElseThrow(UUID id) {
		UserEntity userEntity = userJpaRepository.findById(id).orElseThrow(() ->
			new UserException(UserError.AUTH_USER_NOT_FOUND));

		return userMapper.toDomain(userEntity);
	}

	@Override
	public User findByUserIdOrElseThrow(String userId) {
		UserEntity userEntity = userJpaRepository.findByUserId(userId).orElseThrow(() ->
			new UserException(UserError.AUTH_USER_NOT_FOUND));

		return userMapper.toDomain(userEntity);
	}

	@Override
	public Optional<User> findByUserId(String userId) {
		return userJpaRepository.findByUserId(userId).map(userMapper::toDomain);
	}

	@Override
	public Optional<User> findBySlackId(String slackId) {
		return userJpaRepository.findBySlackId(slackId).map(userMapper::toDomain);
	}

	@Override
	public void save(User user) {
		UserEntity userEntity = userMapper.toEntity(user);

		userJpaRepository.save(userEntity);
	}

	@Override
	public void save(User user, UserCompany newUserCompany) {
		UserEntity userEntity = userMapper.toEntity(user);
		UserCompanyEntity userCompanyEntity = userMapper.toEntity(newUserCompany);

		userEntity.attachUserCompany(userCompanyEntity);

		userJpaRepository.save(userEntity);
	}

	@Override
	public void save(User user, UserDelivery newUserDelivery) {
		UserEntity userEntity = userMapper.toEntity(user);
		UserDeliveryEntity userDeliveryEntity = userMapper.toEntity(newUserDelivery);

		userEntity.attachUserDelivery(userDeliveryEntity);

		userJpaRepository.save(userEntity);
	}

	@Override
	@Transactional
	public void saveStatus(User user, String status) {
		// 영속성 컨텍스트에서 이미 조회된 엔티티를 재사용 (DB 쿼리 없음)
		UserEntity userEntity = userJpaRepository.findById(user.getId())
			.orElseThrow(() -> new UserException(UserError.AUTH_USER_NOT_FOUND));

		StatusEnum statusEnum;
		try {
			statusEnum = StatusEnum.valueOf(status);
		} catch (IllegalArgumentException e) {
			throw new UserException(UserError.USER_INVALID_STATUS);
		}

		userEntity.updateStatus(statusEnum);
	}

	@Override
	public UserCompany findCompanyByUserIdOrElseGetNull(UUID id) {
		UserEntity userEntity = userJpaRepository.findById(id).orElse(null);

		if (userEntity == null || userEntity.getUserCompany() == null) {
			return null;
		}

		return userMapper.toDomain(userEntity.getUserCompany());
	}

	@Override
	public UserDelivery findDeliveryByUserIdOrElseGetNull(UUID id) {
		UserEntity userEntity = userJpaRepository.findById(id).orElse(null);

		if (userEntity == null || userEntity.getUserDelivery() == null) {
			return null;
		}

		return userMapper.toDomain(userEntity.getUserDelivery());
	}
}
