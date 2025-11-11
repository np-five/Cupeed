package com.sparta.cupeed.user.auth.infrastructure.jpa.entity;

public enum RoleEnum {

	MASTER, HUB, DELIVERY, COMPANY;

	public String getAuthority() {
		return "ROLE_" + this.name();
	}
}
