package com.sparta.cupeed.ai.infrastructure.persistence.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.cupeed.ai.infrastructure.persistence.entity.AiEntity;

public interface AiJpaRepository extends JpaRepository<AiEntity, UUID> {
}
