package com.sparta.cupeed.slack.infrastructure.persistence.mapper;

import org.springframework.stereotype.Component;

import com.sparta.cupeed.slack.domain.model.Slack;
import com.sparta.cupeed.slack.infrastructure.persistence.entity.SlackEntity;

@Component
public class SlackMapper {

	public Slack toDomain(SlackEntity entity) {
		if(entity == null) return null;

		return Slack.builder()
			.id(entity.getId())
			.recipientSlackId(entity.getRecipientSlackId())
			.message(entity.getMessage())
			.errorMessage(entity.getErrorMessage())
			.sentAt(entity.getSentAt())
			.status(Slack.Status.valueOf(entity.getStatus().name()))
			.createdAt(entity.getCreatedAt())
			.createdBy(entity.getCreatedBy())
			.updatedAt(entity.getUpdatedAt())
			.updatedBy(entity.getUpdatedBy())
			.deletedAt(entity.getDeletedAt())
			.deletedBy(entity.getDeletedBy())
			.build();
	}

	public SlackEntity toEntity(Slack domain) {
		if(domain == null) return null;

		return SlackEntity.builder()
			.id(domain.getId())
			.recipientSlackId(domain.getRecipientSlackId())
			.message(domain.getMessage())
			.errorMessage(domain.getErrorMessage())
			.sentAt(domain.getSentAt())
			.status(SlackEntity.Status.valueOf(domain.getStatus().name()))
			.build();
	}

}
