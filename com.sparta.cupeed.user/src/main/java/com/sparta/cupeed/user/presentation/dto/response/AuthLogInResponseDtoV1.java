package com.sparta.cupeed.user.presentation.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthLogInResponseDtoV1 {

	private UserDto user;

	public static AuthLogInResponseDtoV1 of(String token) {
		return AuthLogInResponseDtoV1.builder()
			.user(UserDto.from(token))
			.build();
	}

	@Getter
	@Builder
	public static class UserDto {
		private String tokenType;
		private String token;

		public static UserDto from(String token) {
			return UserDto.builder()
				.tokenType("Bearer")
				.token(token)
				.build();
		}
	}
}
