package com.sparta.cupeed.user.auth;

import io.swagger.v3.oas.annotations.media.Schema;

public record AuthSignUpCommand() {

	@Schema(description = "아이디", example = "cupeed123")
	private static String id;

	@Schema(description = "비밀번호", example = "cupeed!234")
	private static String password;

	@Schema(description = "슬랙 아이디", example = "cupeed")
	private static String slackId;

	@Schema(description = "회사 이름", example = "스파르타코딩클럽")
	private static String companyName;

	@Schema(description = "사업자 등록 번호", example = "123-45-67890")
	private static String businessNo;
}
