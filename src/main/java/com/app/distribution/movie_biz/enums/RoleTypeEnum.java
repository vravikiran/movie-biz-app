package com.app.distribution.movie_biz.enums;

public enum RoleTypeEnum {
	SUPER_ADMIN(1, "SUPER_ADMIN"), ADMIN(2, "ADMIN"), INVESTOR(3, "INVESTOR");

	private final int roleid;
	private final String role_name;

	private RoleTypeEnum(int roleid, String role_name) {
		this.roleid = roleid;
		this.role_name = role_name;
	}

	public int getRoleid() {
		return roleid;
	}

	public String getRole_name() {
		return role_name;
	}
}