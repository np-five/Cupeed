package com.sparta.cupeed.company.presentation.controller;

import com.sparta.cupeed.company.application.service.CompanyServiceV1;
import com.sparta.cupeed.company.infrastructure.security.auth.UserDetailsImpl;
import com.sparta.cupeed.company.presentation.controller.code.CompanySuccessCode;
import com.sparta.cupeed.company.presentation.dto.request.CompanyPostRequestDtoV1;
import com.sparta.cupeed.company.presentation.dto.response.CompanyGetResponseDtoV1;
import com.sparta.cupeed.global.response.ApiResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/v1/companies")
@RequiredArgsConstructor
public class CompanyControllerV1 {

	private final CompanyServiceV1 companyService;

	// 1. 업체 생성
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<ApiResponse<Void>> createCompany(@RequestBody CompanyPostRequestDtoV1 requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
		CompanyGetResponseDtoV1 newCompany = companyService.createCompany(requestDto, userDetails);
		URI location = URI.create("/companies/" + newCompany.getCompany().getId());
		return ResponseEntity.created(location).body(ApiResponse.success(CompanySuccessCode.CREATED));
	}

	// 2. 업체 목록 조회 (페이징)
	@GetMapping
	public ResponseEntity<ApiResponse<Page<CompanyGetResponseDtoV1>>> getCompanies(Pageable pageable) {
		return ResponseEntity.ok(ApiResponse.success(CompanySuccessCode.OK, companyService.getCompanies(pageable)));
	}

	// 3. 업체 상세 조회
	@GetMapping("/{companyId}")
	public ResponseEntity<ApiResponse<CompanyGetResponseDtoV1>> getCompany(@PathVariable UUID companyId) {
		return ResponseEntity.ok(ApiResponse.success(CompanySuccessCode.OK, companyService.getCompany(companyId)));
	}

	// 4. 업체 수정
	@PatchMapping("/{companyId}")
	public ResponseEntity<ApiResponse<CompanyGetResponseDtoV1>> updateCompany(
		@PathVariable UUID companyId,
		@RequestBody CompanyPostRequestDtoV1 requestDto,
		@AuthenticationPrincipal UserDetailsImpl userDetails
	) {
		return ResponseEntity.ok(ApiResponse.success(CompanySuccessCode.OK, companyService.updateCompany(companyId, requestDto, userDetails)));
	}

	// 5. 업체 삭제
	@DeleteMapping("/{companyId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<ApiResponse<Void>> deleteCompany(@PathVariable UUID companyId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
		companyService.deleteCompany(companyId, userDetails);
		return ResponseEntity.ok(ApiResponse.success(CompanySuccessCode.OK));
	}
}
