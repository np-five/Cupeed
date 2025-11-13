package com.sparta.cupeed.hub.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// 도메인 계층에서 정의하는 추상적인 저장소 인터페이스
// 인프라에 대한 의존성을 가지지 않습니다.
public interface HubRepository {

	// Hub 단건 조회
	Optional<Hub> findById(UUID id);

	// Hub 전체 목록 조회
	List<Hub> findAll();

	// Hub 저장 (생성 또는 수정)
	Hub save(Hub hub);

	// Hub 삭제
	void delete(Hub hub);

	// HubName 으로 단건 조회 (feignClinet 용)
	Optional<Hub> findByName(String name);

	boolean existsById(UUID hubId);
}

