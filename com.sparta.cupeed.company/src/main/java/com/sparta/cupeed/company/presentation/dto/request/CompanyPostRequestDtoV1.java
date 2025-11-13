package com.sparta.cupeed.company.presentation.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class CompanyPostRequestDtoV1 {

	@NotNull(message = "업체 정보를 입력해주세요.")
	@Valid
	private CompanyDto company;

	@Getter
	@Builder
	public static class CompanyDto {

		@NotBlank(message = "업체 이름을 입력해주세요.")
		private String name;

		@NotBlank(message = "사업자 등록번호를 입력해주세요.")
		private String businessNumber;

		private String address;

		@NotNull(message = "허브 이름을 입력해주세요.")
		private String hubName;

		@NotNull(message = "담당자(대표자) ID를 입력해주세요.")
		private UUID managerId;
	}
}
