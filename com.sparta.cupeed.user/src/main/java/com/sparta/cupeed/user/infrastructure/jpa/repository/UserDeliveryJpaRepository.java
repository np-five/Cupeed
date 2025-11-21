package com.sparta.cupeed.user.infrastructure.jpa.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.cupeed.user.infrastructure.jpa.entity.UserDeliveryEntity;

public interface UserDeliveryJpaRepository extends JpaRepository<UserDeliveryEntity, UUID> {

	List<UserDeliveryEntity> findUserDeliveryEntitiesByUserHubId(UUID hubId);

	List<UserDeliveryEntity> findUserDeliveryEntitiesByUserHubIdIsNull();
}
