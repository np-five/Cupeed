package com.sparta.cupeed.company.presentation.dto.response;

import com.sparta.cupeed.company.domain.model.Company;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
public class CompanyGetResponseDtoV1 {

	private final CompanyDto company;

	public static CompanyGetResponseDtoV1 of(Company company) {
		return CompanyGetResponseDtoV1.builder()
			.company(CompanyDto.from(company))
			.build();
	}

	@Getter
	@Builder
	public static class CompanyDto {
		private final UUID id;
		private final String name;
		private final String businessNumber;
		private final String address;
		private final UUID hubId;
		private final UUID managerId;
		private final Instant createdAt;
		private final Instant updatedAt;

		public static CompanyDto from(Company company) {
			return CompanyDto.builder()
				.id(company.getId())
				.name(company.getName())
				.businessNumber(company.getBusinessNumber())
				.address(company.getAddress())
				.hubId(company.getHubId())
				.managerId(company.getManagerId())
				.createdAt(company.getCreatedAt())
				.updatedAt(company.getUpdatedAt())
				.build();
		}
	}
}
