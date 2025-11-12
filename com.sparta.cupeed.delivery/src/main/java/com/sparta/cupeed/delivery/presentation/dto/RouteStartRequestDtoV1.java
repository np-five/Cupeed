package com.sparta.cupeed.delivery.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RouteStartRequestDtoV1 {

	@NotBlank(message = "배송 담당자 ID를 입력해주세요")
	private String deliveryManagerId;
}