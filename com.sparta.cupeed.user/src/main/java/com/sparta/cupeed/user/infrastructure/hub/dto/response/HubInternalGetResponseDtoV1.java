package com.sparta.cupeed.user.infrastructure.hub.dto.response;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class HubInternalGetResponseDtoV1 {
	private final HubDto hub;

	@Getter
	@Builder
	@ToString
	public static class HubDto {
		private final UUID id;
		private final String name;
		private final String address;
		private final double latitude;
		private final double longitude;
	}
}

