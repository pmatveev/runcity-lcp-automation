package org.runcity.secure;

import java.util.Arrays;

public enum SecureUserRole {
	ADMIN("role.admin"), COORDINATOR("role.coordinator"), USER("role.user");
	private String name;
	
	private SecureUserRole(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public static SecureUserRole getByCode(String code) {
		if (Arrays.binarySearch(values(), code) >= 0) {
			return SecureUserRole.valueOf(code);
		}
		return null;
	}
}
