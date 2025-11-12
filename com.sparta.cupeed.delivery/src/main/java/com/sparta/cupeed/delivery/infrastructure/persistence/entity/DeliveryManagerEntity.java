package com.sparta.cupeed.delivery.infrastructure.persistence.entity;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "p_delivery_manager")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryManagerEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(name = "user_id", length = 100, nullable = false, unique = true)
	private String userId;

	@Column(name = "hub_id", nullable = false)
	private UUID hubId;

	@Enumerated(EnumType.STRING)
	@Column(name = "delivery_type", nullable = false)
	private DeliveryType deliveryType;

	@Column(name = "delivery_sequence")
	private Integer deliverySequence;

	public enum DeliveryType {
		HUB,
		COMPANY
	}

	@PrePersist
	protected void onCreate() {
		if (this.deliverySequence == null) {
			this.deliverySequence = 0;
		}
	}

	public void updateDeliverySequence(Integer newSequence) {
		this.deliverySequence = newSequence;
	}

	public void changeHub(UUID newHubId) {
		this.hubId = newHubId;
	}

	public void changeDeliveryType(DeliveryType newType) {
		this.deliveryType = newType;
	}
}
