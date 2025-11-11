package com.sparta.cupeed.user.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

import com.sparta.cupeed.user.domain.vo.UserRoleEnum;
import com.sparta.cupeed.user.domain.vo.UserStatusEnum;

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
	private final UUID companyId;
	private final UUID hubId;
	private final LocalDateTime createdAt;
	private final String createdBy;
	private final LocalDateTime updatedAt;
	private final String updatedBy;
	private final LocalDateTime deletedAt;
	private final String deletedBy;

	UserBuilder toBuilder() {
		return User.builder()
			.id(id)
			.userId(userId)
			.password(password)
			.slackId(slackId)
			.role(role)
			.status(status)
			.companyId(companyId)
			.hubId(hubId)
			.createdAt(createdAt)
			.createdBy(createdBy)
			.updatedAt(updatedAt)
			.updatedBy(updatedBy)
			.deletedAt(deletedAt)
			.deletedBy(deletedBy);
	}
}
