package com.sparta.cupeed.user.infrastructure.company.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "company")
public interface CompanyClientV1 {

	// TODO: api 구현 필요
	@GetMapping("/internal/v1/companies")
	UUID getInternalCompanyByBusinessNo(@RequestParam String businessNo);
}
