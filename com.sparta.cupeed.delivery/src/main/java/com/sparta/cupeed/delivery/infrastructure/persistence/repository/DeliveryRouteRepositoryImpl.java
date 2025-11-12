package com.sparta.cupeed.delivery.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.cupeed.delivery.domain.model.DeliveryRoute;
import com.sparta.cupeed.delivery.domain.repository.DeliveryRouteRepository;
import com.sparta.cupeed.delivery.infrastructure.persistence.entity.DeliveryRouteEntity;
import com.sparta.cupeed.delivery.infrastructure.persistence.mapper.DeliveryRouteMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeliveryRouteRepositoryImpl implements DeliveryRouteRepository {

	private final DeliveryRouteJpaRepository jpaRepository;
	private final DeliveryRouteMapper mapper;

	@Override
	@Transactional
	public DeliveryRoute save(DeliveryRoute deliveryRoute) {
		DeliveryRouteEntity entity;

		if (deliveryRoute.getId() == null) {
			// 신규 생성
			entity = mapper.toEntity(deliveryRoute);
		} else {
			// 기존 업데이트
			entity = jpaRepository.findById(deliveryRoute.getId())
				.orElseGet(() -> mapper.toEntity(deliveryRoute));
			mapper.applyDomain(deliveryRoute, entity);
		}

		DeliveryRouteEntity saved = jpaRepository.save(entity);
		return mapper.toDomain(saved);
	}

	@Override
	public Optional<DeliveryRoute> findById(UUID id) {
		return jpaRepository.findById(id)
			.map(mapper::toDomain);
	}

	@Override
	public List<DeliveryRoute> findByDeliveryId(UUID deliveryId) {
		return jpaRepository.findByDeliveryId(deliveryId)
			.stream()
			.map(mapper::toDomain)
			.collect(Collectors.toList());
	}

	@Override
	public List<DeliveryRoute> findAll() {
		return jpaRepository.findAll()
			.stream()
			.map(mapper::toDomain)
			.collect(Collectors.toList());
	}
}