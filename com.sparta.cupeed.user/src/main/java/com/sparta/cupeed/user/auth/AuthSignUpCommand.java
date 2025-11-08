package com.sparta.cupeed.user.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public record AuthSignUpCommand() {

	private static String id;
	private static String password;
	private static String slackId;
	private static String companyName;
	private static String businessNo;
}
