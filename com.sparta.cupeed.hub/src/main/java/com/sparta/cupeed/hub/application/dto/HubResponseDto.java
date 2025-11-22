package com.sparta.cupeed.hub.application.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.sparta.cupeed.hub.domain.Hub;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HubResponseDto {
	UUID id;
	String name;
	String address;
	double latitude;
	double longitude;

	LocalDateTime createdAt;
	String createdBy;
	LocalDateTime updatedAt;
	String updatedBy;

	// Hub 엔티티를 DTO로 변환하는 팩토리 메서드
	public static HubResponseDto mapToResponse(Hub hub) {
		return HubResponseDto.builder()
			.id(hub.getId())
			.name(hub.getName())
			.address(hub.getAddress())
			.latitude(hub.getLatitude())
			.longitude(hub.getLongitude())
			.createdAt(hub.getCreatedAt())
			.createdBy(hub.getCreatedBy())
			.updatedAt(hub.getUpdatedAt())
			.updatedBy(hub.getUpdatedBy())
			.build();
	}
}
