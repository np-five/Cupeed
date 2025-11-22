package com.sparta.cupeed.user.infrastructure.jpa.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.cupeed.user.infrastructure.jpa.entity.UserCompanyEntity;

public interface UserCompanyJpaRepository extends JpaRepository<UserCompanyEntity, UUID> {
}
