package com.sparta.cupeed.ai.domain.model;

import java.time.Instant;
import java.util.UUID;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode(of = "id")
public class Ai {
	private final UUID id;
	private final UUID orderId;
	private final String aiResponseText;
	private final Status status;
	private final String errorMessage;
	private final Instant createdAt;
	private final String createdBy;
	private final Instant updatedAt;
	private final String updatedBy;
	private final Instant deletedAt;
	private final String deletedBy;

	public AiBuilder toBuilder() {
		return Ai.builder()
			.id(id)
			.orderId(orderId)
			.aiResponseText(aiResponseText)
			.status(status)
			.errorMessage(errorMessage)
			.createdAt(createdAt)
			.createdBy(createdBy)
			.updatedAt(updatedAt)
			.updatedBy(updatedBy)
			.deletedAt(deletedAt)
			.deletedBy(deletedBy);
	}

	// PENDING -> SUCESS,FAILED로 업데이트할 때 사용
	public Ai withUpdated(Status newStatus, String aiResponseText, String errorMessage) {
		return toBuilder()
			.status(newStatus)
			.aiResponseText(aiResponseText)
			.errorMessage(errorMessage)
			.build();
	}

	public enum Status {
		PENDING,
		SUCCESS,
		FAILED
	}
}
