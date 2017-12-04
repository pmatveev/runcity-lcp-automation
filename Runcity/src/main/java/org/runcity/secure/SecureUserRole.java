package org.runcity.secure;

public enum SecureUserRole {
	ADMIN("role.admin"), VOLUNTEER("role.volunteer");
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
