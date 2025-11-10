package com.sparta.cupeed.hub.infrastructure.jpa;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.cupeed.hub.domain.Hub;

// Spring Data JPA가 제공하는 기본 CRUD 기능을 활용합니다.
// 이 파일은 Domain Layer의 HubRepository가 아니며, Infrastructure Layer에 속합니다.
public interface HubJpaRepository extends JpaRepository<Hub, UUID> {
	// 추가적인 쿼리 메서드가 필요하면 여기에 정의할 수 있습니다.
}