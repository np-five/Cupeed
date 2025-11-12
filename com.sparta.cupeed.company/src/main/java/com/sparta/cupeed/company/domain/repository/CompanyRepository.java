package com.sparta.cupeed.company.domain.repository;

import com.sparta.cupeed.company.domain.model.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository {

	Company save(Company company);

	Optional<Company> findById(UUID id);

	Page<Company> findAll(Pageable pageable);

	boolean existsByBusinessNumber(String businessNumber);

	default Company findByIdOrElseThrow(UUID id) {
		return findById(id)
			.orElseThrow(() -> new RuntimeException("Company not found: " + id));
	}
}
