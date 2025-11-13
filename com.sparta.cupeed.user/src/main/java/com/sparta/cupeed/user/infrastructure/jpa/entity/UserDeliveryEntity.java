package com.sparta.cupeed.user.infrastructure.jpa.entity;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "p_user_delivery")
public class UserDeliveryEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id", nullable = false, updatable = false)
	private UUID id;

	@Column(name = "delivery_type", nullable = false, updatable = false)
	@Enumerated(EnumType.STRING)
	private DeliveryTypeEnum deliveryType;

	@Column(name = "delivery_order", nullable = false)
	private int deliveryOrder;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", unique = true)
	private UserEntity user;

	public void setUser(UserEntity userEntity) {
		this.user = userEntity;
	}
}
