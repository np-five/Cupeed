package com.sparta.cupeed.company.presentation.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.cupeed.company.application.service.CompanyServiceV1;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/internal/v1/companies")
@RequiredArgsConstructor
public class CompanyInternalController {

	private final CompanyServiceV1 companyService;

	@GetMapping
	public UUID getInternalCompanyByBusinessNo(@RequestParam String businessNo) {
		return companyService.getCompanyIdByBusinessNumber(businessNo);
	}
}

