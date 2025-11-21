package com.sparta.cupeed.user.presentation.dto.response;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sparta.cupeed.user.domain.model.User;
import com.sparta.cupeed.user.domain.model.UserCompany;
import com.sparta.cupeed.user.domain.model.UserDelivery;
import com.sparta.cupeed.user.domain.vo.UserRoleEnum;
import com.sparta.cupeed.user.domain.vo.UserStatusEnum;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserGetMyUserResponseDtoV1 {

	UserDto user;
	CompanyDto company;
	HubDto hub;
	DeliveryDto delivery;

	public static UserGetMyUserResponseDtoV1 of(
		User user,
		UserCompany userCompany,
		UserDelivery userDelivery,
		String hubName
	) {
		return UserGetMyUserResponseDtoV1.builder()
			.user(UserDto.from(user))
			.company(CompanyDto.from(userCompany))
			.hub(HubDto.from(user.getHubId(), hubName))
			.delivery(DeliveryDto.from(userDelivery))
			.build();
	}

	@Getter
	@Builder
	public static class UserDto {

		private final UUID id;
		private final String userId;
		private final String slackId;
		private final UserRoleEnum role;
		private final UserStatusEnum status;

		public static UserDto from(User user) {
			return UserDto.builder()
				.id(user.getId())
				.userId(user.getUserId())
				.slackId(user.getSlackId())
				.role(user.getRole())
				.status(user.getStatus())
				.build();
		}
	}

	@Getter
	@Builder
	public static class CompanyDto {

		private final UUID companyId;
		private final String companyName;
		private final String businessNo;

		public static CompanyDto from(UserCompany userCompany) {
			if (userCompany == null) {
				return null;
			}

			return CompanyDto.builder()
				.companyId(userCompany.getId())
				.companyName(userCompany.getCompanyName())
				.businessNo(userCompany.getBusinessNo())
				.build();
		}
	}

	@Getter
	@Builder
	public static class HubDto {

		private final UUID hubId;
		private final String hubName;

		public static HubDto from(UUID id, String hubName) {
			if (id == null || hubName == null) {
				return null;
			}

			return HubDto.builder()
				.hubId(id)
				.hubName(hubName)
				.build();
		}
	}

	@Getter
	@Builder
	private static class DeliveryDto {

		private final UUID deliveryId;
		private final String deliveryType;
		private int deliveryOrder;

		public static DeliveryDto from(UserDelivery delivery) {
			if (delivery == null) {
				return null;
			}

			return DeliveryDto.builder()
				.deliveryId(delivery.getId())
				.deliveryType(delivery.getDeliveryType().name())
				.deliveryOrder(delivery.getDeliveryOrder())
				.build();
		}
	}
}
