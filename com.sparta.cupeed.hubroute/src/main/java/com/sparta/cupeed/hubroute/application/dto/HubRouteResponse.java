package com.sparta.cupeed.hubroute.application.dto;

import java.util.UUID;

import com.sparta.cupeed.hubroute.domain.HubRoute;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HubRouteResponse {
	UUID id;
	UUID startHubId;
	UUID endHubId;
	double duration;
	double distance;

	public static HubRouteResponse mapToResponse(HubRoute hubRoute) {
		return HubRouteResponse.builder()
			.id(hubRoute.getId())
			.startHubId(hubRoute.getStartHubId())
			.endHubId(hubRoute.getEndHubId())
			.duration(hubRoute.getDuration())
			.distance(hubRoute.getDistance())
			.build();
	}
}
