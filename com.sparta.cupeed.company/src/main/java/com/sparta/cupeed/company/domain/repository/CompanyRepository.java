package com.sparta.cupeed.company.domain.repository;

import com.sparta.cupeed.company.domain.model.Company;
import com.sparta.cupeed.company.presentation.controller.code.CompanyErrorCode;
import com.sparta.cupeed.global.exception.BizException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository {

	Company save(Company company);

	Optional<Company> findById(UUID id);
	default Company findByIdOrElseThrow(UUID id) {
		return findById(id)
			.orElseThrow(() -> new BizException(CompanyErrorCode.COMPANY_NOT_FOUND));
	}

	Optional<Company> findByIdAndDeletedAtIsNull(UUID id);
	default Company findByIdAndDeletedAtIsNullOrElseThrow(UUID id) {
		return findByIdAndDeletedAtIsNull(id)
			.orElseThrow(() -> new BizException(CompanyErrorCode.COMPANY_NOT_FOUND));
	}

	Page<Company> findAll(Pageable pageable);
	Page<Company> findAllByDeletedAtIsNull(Pageable pageable);

	Optional<Company> findByBusinessNumber(String businessNumber);

	boolean existsByBusinessNumber(String businessNumber);



	boolean existsById(UUID id);
}
