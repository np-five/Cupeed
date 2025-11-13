package com.sparta.cupeed.product.infrastructure.company.client;

import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "company", path = "/internal/v1/companies")
public interface CompanyClientV1 {

	@GetMapping("/{companyId}")
	UUID getCompany(@PathVariable("companyId") UUID companyId);
}
