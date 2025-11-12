package com.sparta.cupeed.delivery.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.cupeed.delivery.domain.model.DeliveryManager;
import com.sparta.cupeed.delivery.domain.model.DeliveryType;
import com.sparta.cupeed.delivery.domain.repository.DeliveryManagerRepository;
import com.sparta.cupeed.delivery.infrastructure.persistence.entity.DeliveryManagerEntity;
import com.sparta.cupeed.delivery.infrastructure.persistence.mapper.DeliveryManagerMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeliveryManagerRepositoryImpl implements DeliveryManagerRepository {

	private final DeliveryManagerJpaRepository jpaRepository;
	private final DeliveryManagerMapper mapper;

	@Override
	@Transactional
	public DeliveryManager save(DeliveryManager deliveryManager) {
		DeliveryManagerEntity entity;

		if (deliveryManager.getId() == null) {
			// 신규 생성
			entity = mapper.toEntity(deliveryManager);
		} else {
			// 기존 업데이트
			entity = jpaRepository.findById(deliveryManager.getId())
				.orElseGet(() -> mapper.toEntity(deliveryManager));
			mapper.applyDomain(deliveryManager, entity);
		}

		DeliveryManagerEntity saved = jpaRepository.save(entity);
		return mapper.toDomain(saved);
	}

	@Override
	public Optional<DeliveryManager> findById(UUID id) {
		return jpaRepository.findById(id)
			.map(mapper::toDomain);
	}

	@Override
	public Optional<DeliveryManager> findByUserId(String userId) {
		return jpaRepository.findByUserIdAndDeletedAtIsNull(userId)
			.map(mapper::toDomain);
	}

	@Override
	public List<DeliveryManager> findByHubId(UUID hubId) {
		return jpaRepository.findByHubIdAndDeletedAtIsNull(hubId)
			.stream()
			.map(mapper::toDomain)
			.collect(Collectors.toList());
	}

	@Override
	public List<DeliveryManager> findByDeliveryType(DeliveryType deliveryType) {
		DeliveryManagerEntity.DeliveryType entityType =
			DeliveryManagerEntity.DeliveryType.valueOf(deliveryType.name());

		return jpaRepository.findByDeliveryTypeAndDeletedAtIsNull(entityType)
			.stream()
			.map(mapper::toDomain)
			.collect(Collectors.toList());
	}

	@Override
	public List<DeliveryManager> findByHubIdAndDeliveryType(UUID hubId, DeliveryType deliveryType) {
		DeliveryManagerEntity.DeliveryType entityType =
			DeliveryManagerEntity.DeliveryType.valueOf(deliveryType.name());

		return jpaRepository.findByHubIdAndDeliveryType(hubId, entityType)
			.stream()
			.map(mapper::toDomain)
			.collect(Collectors.toList());
	}

	@Override
	public List<DeliveryManager> findAllActive() {
		return jpaRepository.findAllActive()
			.stream()
			.map(mapper::toDomain)
			.collect(Collectors.toList());
	}

	@Override
	@Transactional
	public void delete(DeliveryManager deliveryManager) {
		jpaRepository.findById(deliveryManager.getId())
			.ifPresent(jpaRepository::delete);
	}
}