package com.sparta.cupeed.company.application.service;

import com.sparta.cupeed.company.domain.model.Company;
import com.sparta.cupeed.company.domain.repository.CompanyRepository;
import com.sparta.cupeed.company.infrastructure.hub.client.HubClientV1;
import com.sparta.cupeed.company.infrastructure.hub.client.dto.response.HubInternalResponseDtoV1;
import com.sparta.cupeed.company.infrastructure.security.auth.UserDetailsImpl;
import com.sparta.cupeed.company.infrastructure.user.client.UserClientV1;
import com.sparta.cupeed.company.infrastructure.user.dto.response.InternalUserResponseDtoV1;
import com.sparta.cupeed.company.presentation.controller.code.CompanyErrorCode;
import com.sparta.cupeed.company.presentation.dto.request.CompanyPostRequestDtoV1;
import com.sparta.cupeed.company.presentation.dto.response.CompanyGetResponseDtoV1;
import com.sparta.cupeed.global.exception.BizException;

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
			throw new BizException(CompanyErrorCode.BUSINESS_NUMBER_EXISTS);
		}

		// 2️⃣ 허브 이름으로 허브 ID 조회
		HubInternalResponseDtoV1 findHub = hubClient.getInternalHubIdByName(dto.getHubName());
		if (findHub == null) {
			throw new BizException(CompanyErrorCode.HUB_NOT_FOUND);
		}

		// 3️⃣ 매니저 ID 유효성 확인
		UUID managerId = dto.getManagerId();
		if (managerId == null || !managerId.equals(userDetails.getUserId())) {
			// managerId가 본인과 다르면, 실제 존재하는 유저인지 조회
			InternalUserResponseDtoV1 userResponse = userClient.getInternalUser(managerId);
			if (userResponse == null || userResponse.getUser() == null) {
				throw new BizException(CompanyErrorCode.INVALID_MANAGER);
			}
		}

		// 4️⃣ 회사 생성
		Company newCompany = Company.builder()
			.name(dto.getName())
			.businessNumber(dto.getBusinessNumber())
			.address(dto.getAddress())
			.hubId(findHub.getHub().getId())
			.managerId(managerId)
			.createdBy(userDetails.getUserId())
			.updatedBy(userDetails.getUserId())
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
	public CompanyGetResponseDtoV1 updateCompany(UUID companyId, CompanyPostRequestDtoV1 requestDto, UserDetailsImpl userDetails) {
		Company company = companyRepository.findByIdOrElseThrow(companyId);

		// 자신의 업체만 수정 가능
		if (!company.getManagerId().toString().equals(String.valueOf(userDetails.getId()))) {
			throw new BizException(CompanyErrorCode.NOT_AUTHORIZED);
		}

		CompanyPostRequestDtoV1.CompanyDto dto = requestDto.getCompany();
		HubInternalResponseDtoV1 findHub = hubClient.getInternalHubIdByName(dto.getHubName());

		if (findHub == null) {
			throw new BizException(CompanyErrorCode.HUB_NOT_FOUND);
		}

		Company updated = company.withUpdatedInfo(
			dto.getName(),
			dto.getBusinessNumber(),
			dto.getAddress(),
			findHub.getHub().getId(),
			dto.getManagerId(),
			String.valueOf(userDetails.getId())
		);

		companyRepository.save(updated);
		return CompanyGetResponseDtoV1.of(updated);
	}

	@Transactional
	public void deleteCompany(UUID companyId, UserDetailsImpl userDetails) {
		Company company = companyRepository.findByIdOrElseThrow(companyId);
		Company deleted = company.markDeleted(String.valueOf(userDetails.getId()));
		companyRepository.save(deleted);
	}

	@Transactional(readOnly = true)
	public UUID getCompanyIdByBusinessNumber(String businessNumber) {
		Company company = companyRepository.findByBusinessNumber(businessNumber)
			.orElseThrow(() -> new BizException(CompanyErrorCode.BUSINESS_NUMBER_NOT_FOUND));
		return company.getId();
	}

	@Transactional(readOnly = true)
	public UUID getInternalHubIdByCompanyId(UUID companyId) {
		Company company = companyRepository.findById(companyId)
			.orElseThrow(() -> new BizException(CompanyErrorCode.COMPANY_NOT_FOUND));

		return company.getHubId();
	}
}
