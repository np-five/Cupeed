package com.sparta.cupeed.delivery.infrastructure.persistence.repository;

import com.sparta.cupeed.delivery.domain.model.Delivery;
import com.sparta.cupeed.delivery.domain.model.DeliveryStatus;
import com.sparta.cupeed.delivery.domain.repository.DeliveryRepository;
import com.sparta.cupeed.delivery.infrastructure.persistence.entity.DeliveryJpaEntity;
import com.sparta.cupeed.delivery.infrastructure.persistence.mapper.DeliveryMapper;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class DeliveryRepositoryImpl implements DeliveryRepository {

	private final DeliveryJpaRepository jpaRepository;

	@Override
	public Delivery save(Delivery delivery) {
		// domain → jpa entity
		DeliveryJpaEntity entity = DeliveryMapper.toJpaEntity(delivery);

		DeliveryJpaEntity savedEntity = jpaRepository.save(entity);

		//JPA Entity → domain
		return DeliveryMapper.toDomain(savedEntity);
	}

	@Override
	public Optional<Delivery> findById(UUID id) {
		// JPA Repository로 조회
		Optional<DeliveryJpaEntity> entityOptional = jpaRepository.findActiveById(id);

		// JPA Entity → Domain
		return entityOptional.map(DeliveryMapper::toDomain);
	}

	@Override
	public Optional<Delivery> findByOrderId(UUID orderId) {
		Optional<DeliveryJpaEntity> entityOptional = jpaRepository.findByOrderId(orderId);
		return entityOptional.map(DeliveryMapper::toDomain);
	}

	@Override
	public List<Delivery> findAll(int page, int size) {
		// JPA Repository 페이징 조회
		Page<DeliveryJpaEntity> entityPage = jpaRepository.findAllActive(
			PageRequest.of(page, size)
		);

		// JPA entity → domain
		return entityPage.getContent().stream()
			.map(DeliveryMapper::toDomain)
			.collect(Collectors.toList());
	}

	@Override
	public List<Delivery> findByStatus(DeliveryStatus status, int page, int size) {
		// enum → string
		Page<DeliveryJpaEntity> entityPage = jpaRepository.findByStatus(
			status.name(),
			PageRequest.of(page, size)
		);

		//JPA entity → domain
		return entityPage.getContent().stream()
			.map(DeliveryMapper::toDomain)
			.collect(Collectors.toList());
	}

	@Override
	public long countAll() {
		return jpaRepository.countActive();
	}

	@Override
	public long countByStatus(DeliveryStatus status) {
		return jpaRepository.countByStatusAndActive(status.name());
	}

	@Override
	public void delete(Delivery delivery) {

		DeliveryJpaEntity entity = DeliveryMapper.toJpaEntity(delivery);
		jpaRepository.save(entity);
	}
}