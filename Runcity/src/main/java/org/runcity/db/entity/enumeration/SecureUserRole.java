package org.runcity.db.entity.enumeration;

import java.util.Locale;

import org.springframework.context.MessageSource;

public enum SecureUserRole {
	ADMIN("role.admin", "ADMIN"), VOLUNTEER("role.volunteer", "VOLUNTEER");
	private String displayName;
	private String storedValue;

	private SecureUserRole(String displayName, String storedValue) {
		this.displayName = displayName;
		this.storedValue = storedValue;
	}

	public static String getDisplayName(SecureUserRole data) {
		return data == null ? null : data.displayName;
	}
	
	public static String getDisplayName(SecureUserRole data, MessageSource messageSource, Locale l) {
		return messageSource.getMessage(getDisplayName(data), null, l);
	}

	public static SecureUserRole getByStoredValue(String storedValue) {
		switch (storedValue) {
		case "ADMIN":
			return ADMIN;
		case "VOLUNTEER":
			return VOLUNTEER;
		default:
			return null;
		}
	}

	public static String getStoredValue(SecureUserRole e) {
		return e == null ? null : e.storedValue;
	}
}
