package com.sparta.cupeed.hubroute.application.command;

import java.util.UUID;

import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class UpdateHubRouteCommand {
	UUID startHubId;
	UUID endHubId;

	@Min(value = 0, message = "소요 시간은 0시간 이상이어야 합니다.")
	Double duration;

	@Min(value = 0, message = "거리는 0km 이상이어야 합니다.")
	Double distance;
}
