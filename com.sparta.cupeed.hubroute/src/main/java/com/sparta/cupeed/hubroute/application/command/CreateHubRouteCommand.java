package com.sparta.cupeed.hubroute.application.command;

import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CreateHubRouteCommand {
	@NotNull(message = "시작 Hub ID는 필수입니다.")
	UUID startHubId;

	@NotNull(message = "도착 Hub ID는 필수입니다.")
	UUID endHubId;

	@NotNull(message = "소요 시간은 필수입니다.")
	@Min(value = 0, message = "소요 시간은 0시간 이상이어야 합니다.")
	Double duration;

	@NotNull(message = "거리는 필수입니다.")
	@Min(value = 0, message = "거리는 0km 이상이어야 합니다.")
	Double distance;
}
