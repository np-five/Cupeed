package com.sparta.cupeed.user.auth.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AuthSignUpRequestDtoV1(

	@NotBlank(message = "아이디를 입력해주세요.")
	@Pattern(regexp = "^[a-z0-9_]{3,20}$", message = "아이디는 3~20자의 영소문자, 숫자, 언더바(_)만으로 구성해주세요.")
	@Schema(description = "아이디", example = "cupeed123")
	String userId,

	@NotBlank(message = "비밀번호를 입력해주세요.")
	@Schema(description = "비밀번호", example = "cupeed!234")
	String password,

	@NotBlank(message = "슬랙 아이디를 입력해주세요.")
	@Schema(description = "슬랙 아이디", example = "cupeed")
	String slackId,

	@NotBlank(message = "회사명을 입력해주세요.")
	@Schema(description = "회사명", example = "스파르타코딩클럽")
	String companyName,

	@NotBlank(message = "사업자 등록 번호를 입력해주세요.")
	@Schema(description = "사업자 등록 번호", example = "123-45-67890")
	String businessNo
) {
}
