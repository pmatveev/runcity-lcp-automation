package org.runcity.secure;

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
		try {
			return SecureUserRole.valueOf(code);
		} catch (IllegalArgumentException e) {
		}
		return null;
	}
}
