package com.sparta.cupeed.delivery.infrastructure.persistence.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sparta.cupeed.delivery.infrastructure.persistence.entity.DeliveryRouteEntity;

public interface DeliveryRouteJpaRepository extends JpaRepository<DeliveryRouteEntity, UUID> {

	//배송 ID로 경로 목록 조회
	List<DeliveryRouteEntity> findByDeliveryId(UUID deliveryId);

	//특정 허브에서 출발하는 경로 조회
	List<DeliveryRouteEntity> findByStartHubId(UUID startHubId);

	//특정 허브로 도착하는 경로 조회
	List<DeliveryRouteEntity> findByEndHubId(UUID endHubId);

	//배송 담당자 경로 조회
	@Query("SELECT dr FROM DeliveryRouteEntity dr WHERE dr.deliveryManagerId = :managerId")
	List<DeliveryRouteEntity> findByDeliveryManagerId(@Param("managerId") String managerId);
}
