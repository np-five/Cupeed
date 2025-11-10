package com.sparta.cupeed.hubroute.domain;

import java.util.UUID;

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
@Table(name = "p_hub_route")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HubRoute {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	// 출발 Hub ID (애그리거트 참조)
	@Column(nullable = false)
	private UUID startHubId;

	// 도착 Hub ID (애그리거트 참조)
	@Column(nullable = false)
	private UUID endHubId;

	// 소요 시간 (시간 단위)
	@Column(nullable = false)
	private double duration;

	// 거리 (km 단위)
	@Column(nullable = false)
	private double distance;

	// 생성자
	public HubRoute(UUID startHubId, UUID endHubId, Double duration, double distance) {
		// 비즈니스 유효성 검사
		if (startHubId.equals(endHubId)) {
			throw new IllegalArgumentException("출발지 Hub와 도착지 Hub는 동일할 수 없습니다.");
		}
		if (duration <= 0 || distance <= 0) {
			throw new IllegalArgumentException("소요 시간과 거리는 0보다 커야 합니다.");
		}

		this.startHubId = startHubId;
		this.endHubId = endHubId;
		this.duration = duration;
		this.distance = distance;
	}

	// 비즈니스 메서드 - HubRoute 정보 업데이트
	public void updateInfo(UUID startHubId, UUID endHubId, Double duration, Double distance) {
		if (startHubId != null && endHubId != null && startHubId.equals(endHubId)) {
			throw new IllegalArgumentException("업데이트 시에도 출발지 Hub와 도착지 Hub는 동일할 수 없습니다.");
		}

		if (duration != null && duration <= 0) {
			throw new IllegalArgumentException("업데이트 소요 시간은 0보다 커야 합니다.");
		}

		if (distance != null && distance <= 0) {
			throw new IllegalArgumentException("업데이트 거리는 0보다 커야 합니다.");
		}

		this.startHubId = startHubId != null ? startHubId : this.startHubId;
		this.endHubId = endHubId != null ? endHubId : this.endHubId;
		this.duration = duration != null ? duration : this.duration;
		this.distance = distance != null ? distance : this.distance;
	}
}
