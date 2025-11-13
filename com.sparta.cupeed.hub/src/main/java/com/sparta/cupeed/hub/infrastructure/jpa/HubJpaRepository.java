package com.sparta.cupeed.hub.infrastructure.jpa;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.cupeed.hub.domain.Hub;
import com.sparta.cupeed.hub.infrastructure.persistence.HubEntity;

// Spring Data JPA가 제공하는 기본 CRUD 기능을 활용합니다.
// 이 파일은 Domain Layer의 HubRepository가 아니며, Infrastructure Layer에 속합니다.
public interface HubJpaRepository extends JpaRepository<HubEntity, UUID> {
	Optional<HubEntity> findByName(String name);
}