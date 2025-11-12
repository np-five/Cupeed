package com.sparta.cupeed.user.domain.vo;

public enum UserRoleEnum {

	MASTER, HUB, DELIVERY, COMPANY;

	public String getAuthority() {
		return "ROLE_" + this.name();
	}
}
