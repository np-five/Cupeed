package com.sparta.cupeed.delivery.domain.repository;

import com.sparta.cupeed.delivery.domain.model.Delivery;
import com.sparta.cupeed.delivery.domain.model.DeliveryStatus;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, UUID> {

	// Soft Delete가 적용되지 않은 배송 조회
	@Query("SELECT d FROM Delivery d WHERE d.deletedAt IS NULL")
	Page<Delivery> findAllActive(Pageable pageable);

	// ID로 활성 배송 조회
	@Query("SELECT d FROM Delivery d WHERE d.id = :id AND d.deletedAt IS NULL")
	Optional<Delivery> findActiveById(@Param("id") UUID id);

	// 주문 ID로 배송 조회
	@Query("SELECT d FROM Delivery d WHERE d.orderId = :orderId AND d.deletedAt IS NULL")
	Optional<Delivery> findByOrderId(@Param("orderId") UUID orderId);

	// 배송 상태별 조회
	@Query("SELECT d FROM Delivery d WHERE d.status = :status AND d.deletedAt IS NULL")
	Page<Delivery> findByStatus(@Param("status") DeliveryStatus status, Pageable pageable);

	// 배송 담당자별 배송 조회
	@Query("SELECT d FROM Delivery d WHERE d.deliveryManagerId = :managerId AND d.deletedAt IS NULL")
	Page<Delivery> findByDeliveryManagerId(@Param("managerId") UUID managerId, Pageable pageable);

	// 허브별 배송 조회 (출발 또는 도착)
	@Query("""
		    SELECT d FROM Delivery d 
		    WHERE (d.startHubId = :hubId OR d.endHubId = :hubId) 
		    AND d.deletedAt IS NULL
		""")
	Page<Delivery> findByHubId(@Param("hubId") UUID hubId, Pageable pageable);
}
