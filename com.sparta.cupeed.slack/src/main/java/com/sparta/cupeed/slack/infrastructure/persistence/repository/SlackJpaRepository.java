package com.sparta.cupeed.slack.infrastructure.persistence.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.cupeed.slack.infrastructure.persistence.entity.SlackEntity;

public interface SlackJpaRepository extends JpaRepository<SlackEntity, UUID> {
}
