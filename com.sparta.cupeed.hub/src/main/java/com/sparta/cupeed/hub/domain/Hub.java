package com.sparta.cupeed.hub.domain;

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

// 애그리거트 루트 (Aggregate Root)로 지정되어 외부 접근은 이 엔티티를 통해서만 이루어집니다.
@Entity
@Table(name = "p_hub")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Hub extends BaseEntity {

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

	// 1. 생성자 - Hub 생성 시 사용
	public Hub(String name, String address, Double latitude, Double longitude) {
		if (latitude < -90.0 || latitude > 90.0) {
			throw new IllegalArgumentException("Latitude must be between -90 and 90.");
		}
		if (longitude < -180.0 || longitude > 180.0) {
			throw new IllegalArgumentException("Longitude must be between -180 and 180.");
		}

		this.name = name;
		this.address = address;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	// 2. 비즈니스 메서드 - Hub 정보 업데이트 (데이터 변경 로직)
	public void updateInfo(String name, String address, double latitude, double longitude) {
		if (latitude < -90.0 || latitude > 90.0) {
			throw new IllegalArgumentException("Latitude must be between -90 and 90.");
		}
		if (longitude < -180.0 || longitude > 180.0) {
			throw new IllegalArgumentException("Longitude must be between -180 and 180.");
		}

		this.name = name;
		this.address = address;
		this.latitude = latitude;
		this.longitude = longitude;
	}
}
