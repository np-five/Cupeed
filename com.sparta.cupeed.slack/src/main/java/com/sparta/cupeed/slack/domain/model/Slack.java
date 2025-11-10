package com.sparta.cupeed.slack.domain.model;

import java.time.Instant;
import java.util.UUID;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode(of = "id")
public class Slack {
	private final UUID id;
	private final String recipientSlackId;
	private final String message;
	private final Instant sentAt;
	private final Status status;
	private final String errorMessage;
	private final Instant createdAt;
	private final String createdBy;
	private final Instant updatedAt;
	private final String updatedBy;
	private final Instant deletedAt;
	private final String deletedBy;

	public enum Status {
		REQUESTED,
		SUCCESS,
		FAILED
	}
}
