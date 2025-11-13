package com.sparta.cupeed.user.presentation.dto.request;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record UserUpdateStatusRequestDtoV1(

	@NotBlank(message = "user id(UUID)를 입력해주세요.")
	@Schema(description = "user id(UUID)", example = "e8dc7613-f876-436c-933a-9694cb677c7e")
	UUID id,

	@NotBlank(message = "변경할 상태를 입력해주세요.")
	@Schema(description = "사용자 상태", example = "APPROVED")
	String status
) {
}
