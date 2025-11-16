package com.sparta.cupeed.company.infrastructure.persistence.repository;

import com.sparta.cupeed.company.domain.model.Company;
import com.sparta.cupeed.company.infrastructure.persistence.entity.CompanyEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

import aj.org.objectweb.asm.commons.Remapper;

@Repository
public interface CompanyJpaRepository extends JpaRepository<CompanyEntity, UUID> {
	boolean existsByBusinessNumber(String businessNumber);

	Optional<CompanyEntity> findByBusinessNumber(String businessNumber);

	Optional<CompanyEntity> findByIdAndDeletedAtIsNotNull(UUID id);
	Page<CompanyEntity> findAllByDeletedAtIsNotNull(Pageable pageable);
}
