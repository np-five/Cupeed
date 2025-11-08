package com.sparta.cupeed.ai.infrastructure.persistence.mapper;

import org.springframework.stereotype.Component;

import com.sparta.cupeed.ai.domain.model.Ai;
import com.sparta.cupeed.ai.infrastructure.persistence.entity.AiEntity;

@Component
public class AiMapper {
	public Ai toDomain(AiEntity entity) {
		if(entity == null) return null;

		return Ai.builder()
			.id(entity.getId())
			.orderId(entity.getOrderId())
			.aiResponseText(entity.getAiResponseText())
			.errorMessage(entity.getErrorMessage())
			.status(Ai.Status.valueOf(entity.getStatus().name()))
			.createdAt(entity.getCreatedAt())
			.createdBy(entity.getCreatedBy())
			.updatedAt(entity.getUpdatedAt())
			.updatedBy(entity.getUpdatedBy())
			.deletedAt(entity.getDeletedAt())
			.deletedBy(entity.getDeletedBy())
			.build();
	}

	public AiEntity toEntity(Ai domain) {
		if(domain == null) return null;

		return AiEntity.builder()
			.id(domain.getId())
			.orderId(domain.getOrderId())
			.aiResponseText(domain.getAiResponseText())
			.errorMessage(domain.getErrorMessage())
			.status(AiEntity.Status.valueOf(domain.getStatus().name()))
			.build();
	}
}
