package com.sparta.cupeed.company.infrastructure.persistence.repository;

import com.sparta.cupeed.company.domain.model.Company;
import com.sparta.cupeed.company.domain.repository.CompanyRepository;
import com.sparta.cupeed.company.infrastructure.persistence.entity.CompanyEntity;
import com.sparta.cupeed.company.infrastructure.persistence.mapper.CompanyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyRepositoryImpl implements CompanyRepository {

	private final CompanyJpaRepository companyJpaRepository;
	private final CompanyMapper companyMapper;

	@Override
	@Transactional
	public Company save(Company company) {
		CompanyEntity entity;

		if (company.getId() != null) {
			entity = companyJpaRepository.findById(company.getId())
				.orElseGet(() -> companyMapper.toEntity(company));
			companyMapper.applyDomain(company, entity);
		} else {
			entity = companyMapper.toEntity(company);
		}

		CompanyEntity saved = companyJpaRepository.save(entity);
		return companyMapper.toDomain(saved);
	}

	@Override
	public Optional<Company> findById(UUID id) {
		return companyJpaRepository.findById(id)
			.map(companyMapper::toDomain);
	}

	@Override
	public Optional<Company> findByIdAndDeletedAtIsNull(UUID id) {
		return companyJpaRepository.findByIdAndDeletedAtIsNotNull(id)
			.map(companyMapper::toDomain);
	}

	@Override
	public boolean existsByBusinessNumber(String businessNumber) {
		return companyJpaRepository.existsByBusinessNumber(businessNumber);
	}

	@Override
	public boolean existsById(UUID id) {
		return companyJpaRepository.existsById(id);
	}

	@Override
	public Page<Company> findAll(Pageable pageable) {
		return companyJpaRepository.findAll(pageable)
			.map(companyMapper::toDomain);
	}

	@Override
	public Page<Company> findAllByDeletedAtIsNull(Pageable pageable) {
		return companyJpaRepository.findAllByDeletedAtIsNotNull(pageable)
			.map(companyMapper::toDomain);
	}

	@Override
	public Optional<Company> findByBusinessNumber(String businessNumber) {
		return companyJpaRepository.findByBusinessNumber(businessNumber)
			.map(companyMapper::toDomain);
	}

}
