package com.sparta.cupeed.user.auth.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode(of = "id")
public class UserCompany {

	private final UUID id;
	private final String companyName;
	private final String businessNo;
	private final User user;
	private final LocalDateTime createdAt;
	private final String createdBy;
	private final LocalDateTime updatedAt;
	private final String updatedBy;
	private final LocalDateTime deletedAt;
	private final String deletedBy;
}
