package com.sparta.cupeed.order.infrastructure.security.auth;

public enum RoleEnum {

	MASTER, HUB, DELIVERY, COMPANY;

	public static RoleEnum fromAuthority(String authority) {
		// authority: 'ROLE_MASTER'
		return RoleEnum.valueOf(authority.split("ROLE_")[0]);
	}
}
