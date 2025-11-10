package com.sparta.cupeed.user.auth.infrastructure.jpa.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.cupeed.user.auth.infrastructure.jpa.entity.UserEntity;

public interface UserJpaRepository extends JpaRepository<UserEntity, UUID> {

	Optional<UserEntity> findByUserId(String userId);

	Optional<UserEntity> findBySlackId(String slackId);
}
