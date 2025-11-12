package com.sparta.cupeed.hub.infrastructure;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.sparta.cupeed.hub.domain.Hub;
import com.sparta.cupeed.hub.domain.HubRepository;
import com.sparta.cupeed.hub.infrastructure.jpa.HubJpaRepository;

import lombok.RequiredArgsConstructor;

// Domain Layer의 HubRepository 인터페이스를 구현합니다.
// 내부적으로 JPA 기술을 사용하여 DB에 접근합니다.
@Repository
@RequiredArgsConstructor
public class HubRepositoryImpl implements HubRepository {

	private final HubJpaRepository jpaRepository;

	@Override
	public Optional<Hub> findById(UUID id) {
		return jpaRepository.findById(id);
	}

	@Override
	public List<Hub> findAll() {
		return jpaRepository.findAll();
	}

	@Override
	public Hub save(Hub hub) {
		// JPA의 save 메서드는 영속성 컨텍스트를 통해 Hub 엔티티를 관리합니다.
		return jpaRepository.save(hub);
	}

	@Override
	public void delete(Hub hub) {
		jpaRepository.delete(hub);
	}

	@Override
	public Optional<Hub> findByName(String name) {
		return jpaRepository.findByName(name);
	}
}

