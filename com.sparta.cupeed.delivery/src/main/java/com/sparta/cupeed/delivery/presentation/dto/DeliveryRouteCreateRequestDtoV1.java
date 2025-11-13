package com.sparta.cupeed.delivery.presentation.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryRouteCreateRequestDtoV1 {

	@NotNull(message = "배송 ID를 입력해주세요")
	private UUID deliveryId;

	@NotNull(message = "출발 허브 ID를 입력해주세요")
	private UUID startHubId;

	@NotNull(message = "도착 허브 ID를 입력해주세요")
	private UUID endHubId;

	@Positive(message = "예상 거리는 0보다 커야 합니다")
	private Double estimatedDistance;  // 예상 거리 (km)

	private String estimatedDuration;  // 예상 소요시간 (분)
}
