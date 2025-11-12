package com.sparta.cupeed.company.infrastructure.persistence.repository;

import com.sparta.cupeed.company.infrastructure.persistence.entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CompanyJpaRepository extends JpaRepository<CompanyEntity, UUID> {
	boolean existsByBusinessNumber(String businessNumber);
}
