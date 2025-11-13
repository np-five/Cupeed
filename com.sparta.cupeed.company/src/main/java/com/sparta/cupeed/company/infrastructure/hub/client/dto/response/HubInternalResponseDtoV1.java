package com.sparta.cupeed.company.infrastructure.hub.client.dto.response;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HubInternalResponseDtoV1 {
	private final HubDto hub;

	@Getter
	@Builder
	public static class HubDto {
		private final UUID id;
		private final String name;
		private final String address;
		private final double latitude;
		private final double longitude;
	}
}
