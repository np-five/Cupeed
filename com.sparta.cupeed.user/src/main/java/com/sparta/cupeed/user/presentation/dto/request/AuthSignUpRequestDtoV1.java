package com.sparta.cupeed.user.presentation.dto.request;

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

	@NotBlank(message = "역할을 입력해주세요.")
	@Schema(description = "역할(MASTER, HUB, COMPANY, DELIVERY 중 택일)", example = "MASTER")
	String role,

	// COMPANY
	@Schema(description = "회사명", example = "스파르타코딩클럽")
	String companyName,

	@Schema(description = "사업자 등록 번호", example = "123-45-67890")
	String businessNo,

	// HUB, DELIVERY
	@Schema(description = "허브 이름", example = "인천허브")
	String hubName,

	// DELIVERY
	@Schema(description = "배송기사 타입(HUB, COMPANY)", example = "HUB")
	String deliveryType,

	@Schema(description = "배송기사 순번(1~10)", example = "1")
	Integer deliveryOrder
) {
}
