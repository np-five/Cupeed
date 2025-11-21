package com.sparta.cupeed.user.infrastructure.jpa.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.sparta.cupeed.user.domain.model.UserDelivery;
import com.sparta.cupeed.user.domain.repository.UserDeliveryRepository;
import com.sparta.cupeed.user.infrastructure.jpa.entity.UserDeliveryEntity;
import com.sparta.cupeed.user.infrastructure.jpa.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserDeliveryRepositoryImpl implements UserDeliveryRepository {

	private final UserDeliveryJpaRepository userDeliveryJpaRepository;
	private final UserMapper userMapper;

	@Override
	public List<UserDelivery> findUserDeliveryEntitiesByUserHubId(UUID hubId) {
		List<UserDeliveryEntity> userDeliveryEntityList
			= userDeliveryJpaRepository.findUserDeliveryEntitiesByUserHubId(hubId);

		return userDeliveryEntityList.stream().map(userMapper::toDomain).toList();
	}

	@Override
	public List<UserDelivery> findUserDeliveryEntitiesByUserHubIdIsNull() {
		List<UserDeliveryEntity> userDeliveryEntityList
			= userDeliveryJpaRepository.findUserDeliveryEntitiesByUserHubIdIsNull();

		return userDeliveryEntityList.stream().map(userMapper::toDomain).toList();
	}
}
