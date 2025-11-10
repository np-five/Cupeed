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

	public enum Status {
		SUCCESS,
		FAILED
	}
}
