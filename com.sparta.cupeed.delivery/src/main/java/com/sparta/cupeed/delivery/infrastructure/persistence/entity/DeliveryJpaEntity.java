package com.sparta.cupeed.delivery.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "p_delivery")
public class DeliveryJpaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(columnDefinition = "UUID")
	private UUID id;

	@Column(name = "order_id", columnDefinition = "UUID", nullable = false)
	private UUID orderId;

	@Column(name = "receive_company_id", columnDefinition = "UUID", nullable = false)
	private UUID receiveCompanyId;

	@Column(name = "start_hub_id", columnDefinition = "UUID", nullable = false)
	private UUID startHubId;

	@Column(name = "end_hub_id", columnDefinition = "UUID", nullable = false)
	private UUID endHubId;

	@Enumerated(EnumType.STRING)
	@Column(length = 100, nullable = false)
	private DeliveryStatus status;

	@Column(name = "delivery_manager_id", columnDefinition = "UUID")
	private UUID deliveryManagerId;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@Column(name = "created_by", length = 100, nullable = false)
	private String createdBy;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@Column(name = "updated_by", length = 100)
	private String updatedBy;

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;

	@Column(name = "deleted_by", length = 100)
	private String deletedBy;

	public enum DeliveryStatus {
		READY,              // 허브 대기중
		HUB_TRANSIT,        // 허브 이동중
		HUB_ARRIVED,        // 목적지 허브 도착
		COMPANY_TRANSIT,    // 업체 이동중
		DELIVERED           // 배송 완료
	}
}