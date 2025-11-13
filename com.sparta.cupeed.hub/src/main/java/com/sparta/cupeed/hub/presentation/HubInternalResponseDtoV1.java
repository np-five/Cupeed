package com.sparta.cupeed.hub.presentation;

import java.util.UUID;

import com.sparta.cupeed.hub.domain.Hub;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HubInternalResponseDtoV1 {
	private final HubDto hub;

	public static HubInternalResponseDtoV1 of(Hub hub) {
		return HubInternalResponseDtoV1.builder()
			.hub(HubDto.from(hub))
			.build();
	}

	@Getter
	@Builder
	public static class HubDto {
		private final UUID id;
		private final String name;
		private final String address;
		private final double latitude;
		private final double longitude;

		public static HubDto from(Hub hub) {
			return HubDto.builder()
				.id(hub.getId())
				.name(hub.getName())
				.address(hub.getAddress())
				.latitude(hub.getLatitude())
				.longitude(hub.getLongitude())
				.build();
		}
	}
}
