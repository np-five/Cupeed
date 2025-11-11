package com.sparta.cupeed.user.infrastructure.jpa.mapper;

import org.springframework.stereotype.Component;

import com.sparta.cupeed.user.auth.domain.model.User;
import com.sparta.cupeed.user.auth.domain.model.UserCompany;
import com.sparta.cupeed.user.auth.domain.vo.UserRoleEnum;
import com.sparta.cupeed.user.auth.domain.vo.UserStatusEnum;
import com.sparta.cupeed.user.infrastructure.jpa.entity.RoleEnum;
import com.sparta.cupeed.user.infrastructure.jpa.entity.StatusEnum;
import com.sparta.cupeed.user.infrastructure.jpa.entity.UserCompanyEntity;
import com.sparta.cupeed.user.infrastructure.jpa.entity.UserEntity;

@Component
public class UserMapper {

	public User toDomain(UserEntity userEntity) {
		return User.builder()
			.id(userEntity.getId())
			.userId(userEntity.getUserId())
			.password(userEntity.getPassword())
			.slackId(userEntity.getSlackId())
			.role(UserRoleEnum.valueOf(userEntity.getRole().name()))
			.status(UserStatusEnum.valueOf(userEntity.getStatus().name()))
			.createdAt(userEntity.getCreatedAt())
			.createdBy(userEntity.getCreatedBy())
			.updatedAt(userEntity.getUpdatedAt())
			.updatedBy(userEntity.getUpdatedBy())
			.deletedAt(userEntity.getDeletedAt())
			.deletedBy(userEntity.getDeletedBy())
			.build();
	}

	public UserCompany toDomain(UserCompanyEntity userCompanyEntity) {
		return UserCompany.builder()
			.id(userCompanyEntity.getId())
			.user(toDomain(userCompanyEntity.getUser()))
			.companyName(userCompanyEntity.getCompanyName())
			.businessNo(userCompanyEntity.getBusinessNo())
			.createdAt(userCompanyEntity.getCreatedAt())
			.createdBy(userCompanyEntity.getCreatedBy())
			.updatedAt(userCompanyEntity.getUpdatedAt())
			.updatedBy(userCompanyEntity.getUpdatedBy())
			.deletedAt(userCompanyEntity.getDeletedAt())
			.deletedBy(userCompanyEntity.getDeletedBy())
			.build();
	}

	public UserEntity toEntity(User user) {
		return UserEntity.builder()
			.id(user.getId())
			.userId(user.getUserId())
			.password(user.getPassword())
			.slackId(user.getSlackId())
			.role(RoleEnum.valueOf(user.getRole().name()))
			.status(StatusEnum.valueOf(user.getStatus().name()))
			.build();
	}

	public UserCompanyEntity toEntity(UserCompany userCompany) {
		return UserCompanyEntity.builder()
			.id(userCompany.getId())
			.companyName(userCompany.getCompanyName())
			.businessNo(userCompany.getBusinessNo())
			.build();
	}

	// public UserEntity toEntity(User user) {
	// 	return UserEntity.builder()
	// 		.id(user.getId())
	// 		.userId(user.getUserId())
	// 		.password(user.getPassword())
	// 		.slackId(user.getSlackId())
	// 		.role(user.getRole())
	// 		.status(user.getStatus())
	// 		.createdAt(user.getCreatedAt())
	// 		.createdBy(user.getCreatedBy())
	// 		.updatedAt(user.getUpdatedAt())
	// 		.updatedBy(user.getUpdatedBy())
	// 		.deletedAt(user.getDeletedAt())
	// 		.deletedBy(user.getDeletedBy())
	// 		.build();
	// }
}
