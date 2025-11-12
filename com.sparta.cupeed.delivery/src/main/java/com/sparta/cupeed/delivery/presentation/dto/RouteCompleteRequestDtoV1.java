package com.sparta.cupeed.delivery.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RouteCompleteRequestDtoV1 {

	//배송 담당자가 배송 완료 시 실제 거리와 소요시간을 입력
	@NotNull(message = "실제 이동 거리를 입력해주세요")
	@Positive(message = "거리는 0보다 커야 합니다")
	private Double actualDistance;  // 실제 이동 거리 (km)
	@NotBlank(message = "실제 소요시간을 입력해주세요")
	private String actualDuration;  // 실제 소요시간 (분)
}