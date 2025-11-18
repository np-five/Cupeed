package com.sparta.cupeed.order.infrastructure.security;

public enum RoleEnum {

	MASTER, HUB, DELIVERY, COMPANY;

	public static RoleEnum fromAuthority(String authority) {
		// authority: 'ROLE_MASTER'
		// return RoleEnum.valueOf(authority.split("ROLE_")[0]);
		// TODO 위의 코드가 원본 코드
		return RoleEnum.valueOf(authority.split("_")[1]);
	}
}
