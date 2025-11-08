package com.sparta.cupeed.hub.application.dto;

import java.util.UUID;

import com.sparta.cupeed.hub.domain.Hub;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HubResponseDto {
	UUID id;
	String name;
	String address;
	double latitude;
	double longitude;

	// Hub 엔티티를 DTO로 변환하는 팩토리 메서드
	public static HubResponseDto mapToResponse(Hub hub) {
		return HubResponseDto.builder()
			.id(hub.getId())
			.name(hub.getName())
			.address(hub.getAddress())
			.latitude(hub.getLatitude())
			.longitude(hub.getLongitude())
			.build();
	}

}
