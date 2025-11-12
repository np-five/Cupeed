package com.sparta.cupeed.company.application.service;

import com.sparta.cupeed.company.domain.model.Company;
import com.sparta.cupeed.company.domain.repository.CompanyRepository;
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

	@Transactional
	public CompanyGetResponseDtoV1 createCompany(CompanyPostRequestDtoV1 requestDto) {
		CompanyPostRequestDtoV1.CompanyDto dto = requestDto.getCompany();

		// 중복 사업자번호 체크
		if (companyRepository.existsByBusinessNumber(dto.getBusinessNumber())) {
			throw new IllegalArgumentException("이미 존재하는 사업자 등록번호입니다.");
		}

		Company newCompany = Company.builder()
			.name(dto.getName())
			.businessNumber(dto.getBusinessNumber())
			.address(dto.getAddress())
			.hubId(dto.getHubId())
			.managerId(dto.getManagerId())
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

		Company updated = company.withUpdatedInfo(
			dto.getName(),
			dto.getBusinessNumber(),
			dto.getAddress(),
			dto.getHubId(),
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
}
