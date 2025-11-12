package com.sparta.cupeed.hub.infrastructure.persistence;

import java.util.UUID;

import com.sparta.cupeed.hub.common.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "p_hub")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HubEntity extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(nullable = false, unique = true, length = 100)
	private String name;

	@Column(nullable = false)
	private String address;

	// 위도
	@Column(nullable = false)
	private double latitude;

	// 경도
	@Column(nullable = false)
	private double longitude;
}
