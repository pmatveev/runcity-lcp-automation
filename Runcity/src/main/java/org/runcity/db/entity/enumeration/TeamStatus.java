package org.runcity.db.entity.enumeration;

import java.util.Locale;

import org.springframework.context.MessageSource;

public enum TeamStatus {
	ACTIVE("teamStatus.active", "0");

	private String displayName;
	private String storedValue;

	private TeamStatus(String displayName, String storedValue) {
		this.displayName = displayName;
		this.storedValue = storedValue;
	}

	public String getDisplayName() {
		return displayName;
	}
	
	public String getDisplayName(MessageSource messageSource, Locale l) {
		return messageSource.getMessage(displayName, null, l);
	}

	private static boolean isNumber(String value) {
		if (value == null) {
			return false;
		}
		for (int i = 0; i < value.length(); i++) {
			char c = value.charAt(i);
			if (c < '0' || c > '9') {
				return true;
			}
		}
		return false;
	}
	
	public static TeamStatus getByStoredValue(String storedValue) {
		if (isNumber(storedValue)) {
			return ACTIVE;
		}
		switch (storedValue) {
		default:
			return null;
		}
	}

	public static String getStoredValue(TeamStatus e) {
		return e == null ? null : e.storedValue;
	}
}
