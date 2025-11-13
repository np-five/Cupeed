package com.sparta.cupeed.company.presentation.controller;

import com.sparta.cupeed.company.application.service.CompanyServiceV1;
import com.sparta.cupeed.company.presentation.dto.request.CompanyPostRequestDtoV1;
import com.sparta.cupeed.company.presentation.dto.response.CompanyGetResponseDtoV1;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/companies")
@RequiredArgsConstructor
public class CompanyControllerV1 {

	private final CompanyServiceV1 companyService;

	// 1. 업체 생성
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CompanyGetResponseDtoV1 createCompany(@RequestBody CompanyPostRequestDtoV1 requestDto) {
		return companyService.createCompany(requestDto);
	}

	// 2. 업체 목록 조회 (페이징)
	@GetMapping
	public Page<CompanyGetResponseDtoV1> getCompanies(Pageable pageable) {
		return companyService.getCompanies(pageable);
	}

	// 3. 업체 상세 조회
	@GetMapping("/{companyId}")
	public CompanyGetResponseDtoV1 getCompany(@PathVariable UUID companyId) {
		return companyService.getCompany(companyId);
	}

	// 4. 업체 수정
	@PatchMapping("/{companyId}")
	public CompanyGetResponseDtoV1 updateCompany(
		@PathVariable UUID companyId,
		@RequestBody CompanyPostRequestDtoV1 requestDto
	) {
		return companyService.updateCompany(companyId, requestDto);
	}

	// 5. 업체 삭제
	@DeleteMapping("/{companyId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteCompany(@PathVariable UUID companyId) {
		companyService.deleteCompany(companyId);
	}
}
