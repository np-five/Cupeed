package com.sparta.cupeed.hub.infrastructure.persistence;

import org.springframework.stereotype.Component;

import com.sparta.cupeed.hub.domain.Hub;

@Component
public class HubMapper {

	public HubEntity toEntity(Hub domain) {
		if (domain == null) {
			return null;
		}

		return HubEntity.builder()
			.id(domain.getId())
			.name(domain.getName())
			.address(domain.getAddress())
			.latitude(domain.getLatitude())
			.longitude(domain.getLongitude())
			.build();
	}

	public void updateEntityFromDomain(Hub domain, HubEntity entity) {
		entity.setName(domain.getName());
		entity.setAddress(domain.getAddress());
		entity.setLatitude(domain.getLatitude());
		entity.setLongitude(domain.getLongitude());
	}

	public Hub toDomain(HubEntity entity) {
		if (entity == null) {
			return null;
		}

		Hub domain = Hub.builder()
			.id(entity.getId())
			.name(entity.getName())
			.address(entity.getAddress())
			.latitude(entity.getLatitude())
			.longitude(entity.getLongitude())
			.build();
		domain.setCreatedAt(entity.getCreatedAt());
		domain.setCreatedBy(entity.getCreatedBy());
		domain.setUpdatedAt(entity.getUpdatedAt());
		domain.setUpdatedBy(entity.getUpdatedBy());
		domain.setDeletedAt(entity.getDeletedAt());
		domain.setDeletedBy(entity.getDeletedBy());

		return domain;
	}
}

