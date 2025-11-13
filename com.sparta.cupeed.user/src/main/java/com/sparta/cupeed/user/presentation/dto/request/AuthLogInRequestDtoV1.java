package com.sparta.cupeed.user.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AuthLogInRequestDtoV1(

	@NotBlank(message = "아이디를 입력해주세요.")
	@Pattern(regexp = "^[a-z0-9_]{3,20}$", message = "아이디는 3~20자의 영소문자, 숫자, 언더바(_)만으로 구성해주세요.")
	@Schema(description = "아이디", example = "cupeed123")
	String userId,

	@NotBlank(message = "비밀번호를 입력해주세요.")
	@Schema(description = "비밀번호", example = "cupeed!234")
	String password
) {
}
