package com.sparta.cupeed.user.auth.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

import com.sparta.cupeed.user.auth.domain.vo.UserRoleEnum;
import com.sparta.cupeed.user.auth.domain.vo.UserStatusEnum;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@EqualsAndHashCode(of = "id")
@ToString
public class User {

	private final UUID id;
	private final String userId;
	private final String password;
	private final String slackId;
	private final UserRoleEnum role;
	private final UserStatusEnum status;
	private final LocalDateTime createdAt;
	private final String createdBy;
	private final LocalDateTime updatedAt;
	private final String updatedBy;
	private final LocalDateTime deletedAt;
	private final String deletedBy;
}
