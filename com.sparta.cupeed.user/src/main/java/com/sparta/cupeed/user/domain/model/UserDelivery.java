package com.sparta.cupeed.user.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

import com.sparta.cupeed.user.domain.vo.UserDeliveryTypeEnum;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode(of = "id")
public class UserDelivery {

	private final UUID id;
	private final UserDeliveryTypeEnum deliveryType;
	private final int deliveryOrder;
	private final User user;
	private final LocalDateTime createdAt;
	private final String createdBy;
	private final LocalDateTime updatedAt;
	private final String updatedBy;
	private final LocalDateTime deletedAt;
	private final String deletedBy;
}
