package com.sparta.cupeed.hub.infrastructure.jpa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.sparta.cupeed.hub.domain.Hub;
import com.sparta.cupeed.hub.domain.HubRepository;
import com.sparta.cupeed.hub.infrastructure.persistence.HubEntity;
import com.sparta.cupeed.hub.infrastructure.persistence.HubMapper;

import lombok.RequiredArgsConstructor;

// Domain Layer의 HubRepository 인터페이스를 구현합니다.
// 내부적으로 JPA 기술을 사용하여 DB에 접근합니다.
@Repository
@RequiredArgsConstructor
public class HubRepositoryImpl implements HubRepository {

	private final HubJpaRepository jpaRepository;
	private final HubMapper hubMapper;

	@Override
	public Optional<Hub> findById(UUID id) {
		return jpaRepository.findById(id).map(hubMapper::toDomain);
	}

	@Override
	public List<Hub> findAll() {
		return jpaRepository.findAll().stream()
			.map(hubMapper::toDomain)
			.collect(Collectors.toList());
	}

	@Override
	public Hub save(Hub hub) {
		// if (hub.getId() == null) {
		// 생성
		HubEntity newEntity = hubMapper.toEntity(hub);
		HubEntity savedEntity = jpaRepository.save(newEntity);
		return hubMapper.toDomain(savedEntity);
		// } else {
		// 	// 수정
		// 	HubEntity existingEntity = jpaRepository.findById(hub.getId())
		// 		.orElseThrow(() -> new IllegalArgumentException("Hub not found for update: " + hub.getId()));
		// 	hubMapper.updateEntityFromDomain(hub, existingEntity);
		// 	// soft-delete 필드 업데이트
		// 	if (hub.getDeletedAt() != null) {
		// 		existingEntity.softDelete(hub.getDeletedBy());
		// 	}
		// 	HubEntity savedEntity = jpaRepository.save(existingEntity);
		// 	return hubMapper.toDomain(savedEntity);
		// }
	}

	@Override
	public void delete(Hub hub) {
		jpaRepository.delete(hubMapper.toEntity(hub));
	}

	@Override
	public Optional<Hub> findByName(String name) {
		return jpaRepository.findByName(name).map(hubMapper::toDomain);
	}
}

