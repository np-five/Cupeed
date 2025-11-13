package com.sparta.cupeed.delivery.infrastructure.persistence.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sparta.cupeed.delivery.infrastructure.persistence.entity.DeliveryManagerEntity;
import com.sparta.cupeed.delivery.infrastructure.persistence.entity.DeliveryManagerEntity.DeliveryType;

public interface DeliveryManagerJpaRepository extends JpaRepository<DeliveryManagerEntity, UUID> {

	Optional<DeliveryManagerEntity> findByUserIdAndDeletedAtIsNull(String userId);

	List<DeliveryManagerEntity> findByHubIdAndDeletedAtIsNull(UUID hubId);

	List<DeliveryManagerEntity> findByDeliveryTypeAndDeletedAtIsNull(DeliveryType deliveryType);

	@Query("SELECT dm FROM DeliveryManagerEntity dm WHERE dm.hubId = :hubId AND dm.deliveryType = :deliveryType AND dm.deletedAt IS NULL ORDER BY dm.deliverySequence")
	List<DeliveryManagerEntity> findByHubIdAndDeliveryType(
		@Param("hubId") UUID hubId,
		@Param("deliveryType") DeliveryType deliveryType
	);

	@Query("SELECT dm FROM DeliveryManagerEntity dm WHERE dm.deletedAt IS NULL ORDER BY dm.hubId, dm.deliverySequence")
	List<DeliveryManagerEntity> findAllActive();

	@Query("SELECT COALESCE(MAX(dm.deliverySequence), 0) + 1 FROM DeliveryManagerEntity dm WHERE dm.hubId = :hubId AND dm.deletedAt IS NULL")
	Integer findNextSequenceByHubId(@Param("hubId") UUID hubId);
}