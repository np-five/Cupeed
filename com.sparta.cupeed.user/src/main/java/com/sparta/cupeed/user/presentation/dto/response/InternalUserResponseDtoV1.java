package com.sparta.cupeed.user.presentation.dto.response;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class InternalUserResponseDtoV1 {

	private UserDto user;

	@Getter
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	@Builder
	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class UserDto {
		private UUID id;
		private UUID companyId;
	}
}
