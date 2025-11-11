package com.sparta.cupeed.delivery.infrastructure.persistence.repository;

import com.sparta.cupeed.delivery.infrastructure.persistence.entity.DeliveryJpaEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface DeliveryJpaRepository extends JpaRepository<DeliveryJpaEntity, UUID> {

	@Query("SELECT d FROM DeliveryJpaEntity d WHERE d.deletedAt IS NULL")
	Page<DeliveryJpaEntity> findAllActive(Pageable pageable);

	@Query("SELECT d FROM DeliveryJpaEntity d WHERE d.id = :id AND d.deletedAt IS NULL")
	Optional<DeliveryJpaEntity> findActiveById(@Param("id") UUID id);

	@Query("SELECT d FROM DeliveryJpaEntity d WHERE d.orderId = :orderId AND d.deletedAt IS NULL")
	Optional<DeliveryJpaEntity> findByOrderId(@Param("orderId") UUID orderId);

	@Query("SELECT d FROM DeliveryJpaEntity d WHERE d.status = :status AND d.deletedAt IS NULL")
	Page<DeliveryJpaEntity> findByStatus(@Param("status") String status, Pageable pageable);

	@Query("SELECT COUNT(d) FROM DeliveryJpaEntity d WHERE d.deletedAt IS NULL")
	long countActive();

	@Query("SELECT COUNT(d) FROM DeliveryJpaEntity d WHERE d.status = :status AND d.deletedAt IS NULL")
	long countByStatusAndActive(@Param("status") String status);
}
