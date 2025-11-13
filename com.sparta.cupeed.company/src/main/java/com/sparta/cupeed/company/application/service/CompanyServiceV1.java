package com.sparta.cupeed.company.application.service;

import com.sparta.cupeed.company.domain.model.Company;
import com.sparta.cupeed.company.domain.repository.CompanyRepository;
import com.sparta.cupeed.company.infrastructure.hub.client.HubClientV1;
import com.sparta.cupeed.company.infrastructure.hub.client.dto.response.HubInternalResponseDtoV1;
import com.sparta.cupeed.company.infrastructure.security.auth.UserDetailsImpl;
import com.sparta.cupeed.company.infrastructure.user.client.UserClientV1;
import com.sparta.cupeed.company.infrastructure.user.dto.response.InternalUserResponseDtoV1;
import com.sparta.cupeed.company.presentation.dto.request.CompanyPostRequestDtoV1;
import com.sparta.cupeed.company.presentation.dto.response.CompanyGetResponseDtoV1;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyServiceV1 {

	private final CompanyRepository companyRepository;
	private final UserClientV1 userClient;
	private final HubClientV1 hubClient;

	@Transactional
	public CompanyGetResponseDtoV1 createCompany(CompanyPostRequestDtoV1 requestDto, UserDetailsImpl userDetails) {
		CompanyPostRequestDtoV1.CompanyDto dto = requestDto.getCompany();

		// 1️⃣ 중복 사업자번호 체크
		if (companyRepository.existsByBusinessNumber(dto.getBusinessNumber())) {
			throw new IllegalArgumentException("이미 존재하는 사업자 등록번호입니다.");
		}

		// 2️⃣ 허브 이름으로 허브 ID 조회
		HubInternalResponseDtoV1 findHub = hubClient.getInternalHubIdByName(dto.getHubName());
		if (findHub == null) {
			throw new IllegalArgumentException("해당 허브 이름으로 허브를 찾을 수 없습니다: " + dto.getHubName());
		}

		// 3️⃣ 매니저 ID 유효성 확인
		UUID managerId = dto.getManagerId();
		if (managerId == null || !managerId.equals(userDetails.getUserId())) {
			// managerId가 본인과 다르면, 실제 존재하는 유저인지 조회
			InternalUserResponseDtoV1 userResponse = userClient.getInternalUser(managerId);
			if (userResponse == null || userResponse.getUser() == null) {
				throw new IllegalArgumentException("존재하지 않는 사용자입니다. managerId=" + managerId);
			}
		}

		// 4️⃣ 회사 생성
		Company newCompany = Company.builder()
			.name(dto.getName())
			.businessNumber(dto.getBusinessNumber())
			.address(dto.getAddress())
			.hubId(findHub.getHub().getId())
			.managerId(managerId)
			.build();

		Company savedCompany = companyRepository.save(newCompany);
		return CompanyGetResponseDtoV1.of(savedCompany);
	}

	@Transactional(readOnly = true)
	public CompanyGetResponseDtoV1 getCompany(UUID companyId) {
		Company company = companyRepository.findByIdOrElseThrow(companyId);
		return CompanyGetResponseDtoV1.of(company);
	}

	@Transactional(readOnly = true)
	public Page<CompanyGetResponseDtoV1> getCompanies(Pageable pageable) {
		Page<Company> page = companyRepository.findAll(pageable);
		return page.map(CompanyGetResponseDtoV1::of);
	}

	@Transactional
	public CompanyGetResponseDtoV1 updateCompany(UUID companyId, CompanyPostRequestDtoV1 requestDto) {
		Company company = companyRepository.findByIdOrElseThrow(companyId);
		CompanyPostRequestDtoV1.CompanyDto dto = requestDto.getCompany();
		HubInternalResponseDtoV1 findhub = hubClient.getInternalHubIdByName(dto.getHubName());

		Company updated = company.withUpdatedInfo(
			dto.getName(),
			dto.getBusinessNumber(),
			dto.getAddress(),
			findhub.getHub().getId(),
			dto.getManagerId()
		);

		companyRepository.save(updated);
		return CompanyGetResponseDtoV1.of(updated);
	}

	@Transactional
	public void deleteCompany(UUID companyId) {
		Company company = companyRepository.findByIdOrElseThrow(companyId);
		// TODO deletedBy 수정 필요
		Company deleted = company.markDeleted();
		companyRepository.save(deleted); // @Transactional 내에서 dirty checking으로 update 됨, save() 생략 가능
	}

	@Transactional(readOnly = true)
	public UUID getCompanyIdByBusinessNumber(String businessNumber) {
		Company company = companyRepository.findByBusinessNumber(businessNumber)
			.orElseThrow(() -> new RuntimeException("Company not found: " + businessNumber));
		return company.getId();
	}

	@Transactional(readOnly = true)
	public UUID getInternalHubIdByCompanyId(UUID companyId) {
		Company company = companyRepository.findById(companyId)
			.orElseThrow(() -> new IllegalArgumentException("Company not found: " + companyId));

		return company.getHubId();
	}
}
