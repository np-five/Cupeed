package com.sparta.cupeed.company.infrastructure.persistence.mapper;

import com.sparta.cupeed.company.domain.model.Company;
import com.sparta.cupeed.company.infrastructure.persistence.entity.CompanyEntity;
import org.springframework.stereotype.Component;

@Component
public class CompanyMapper {

	public Company toDomain(CompanyEntity entity) {
		if (entity == null) return null;

		return Company.builder()
			.id(entity.getId())
			.name(entity.getName())
			.businessNumber(entity.getBusinessNumber())
			.address(entity.getAddress())
			.hubId(entity.getHubId())
			.managerId(entity.getManagerId())
			.createdAt(entity.getCreatedAt())
			.createdBy(entity.getCreatedBy())
			.updatedAt(entity.getUpdatedAt())
			.updatedBy(entity.getUpdatedBy())
			.deletedAt(entity.getDeletedAt())
			.deletedBy(entity.getDeletedBy())
			.build();
	}

	public CompanyEntity toEntity(Company domain) {
		if (domain == null) return null;

		return CompanyEntity.builder()
			.id(domain.getId())
			.name(domain.getName())
			.businessNumber(domain.getBusinessNumber())
			.address(domain.getAddress())
			.hubId(domain.getHubId())
			.managerId(domain.getManagerId())
			.build();
	}

	public void applyDomain(Company domain, CompanyEntity entity) {
		if (domain == null || entity == null) return;
		entity.updateInfo(
			domain.getName(),
			domain.getBusinessNumber(),
			domain.getAddress(),
			domain.getHubId(),
			domain.getManagerId()
		);
	}
}
