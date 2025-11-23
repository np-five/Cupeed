package com.sparta.cupeed.ai.infrastructure.security.auth;

public enum RoleEnum {

	MASTER, HUB, DELIVERY, COMPANY;

	public static RoleEnum fromAuthority(String authority) {
		return RoleEnum.valueOf(authority.split("ROLE_")[1]);
	}
}
