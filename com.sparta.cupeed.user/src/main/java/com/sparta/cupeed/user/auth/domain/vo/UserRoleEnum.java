package com.sparta.cupeed.user.auth.domain.vo;

public enum UserRoleEnum {

	MASTER, HUB, DELIVERY, COMPANY;

	public String getAuthority() {
		return "ROLE_" + this.name();
	}
}
