package org.runcity.db.entity.enumeration;

import java.util.Locale;

import org.springframework.context.MessageSource;

public enum EventType {
	VOLUNTEER_AT_CP("eventType.volunteerCP", "V");

	private String displayName;
	private String storedValue;

	private EventType(String displayName, String storedValue) {
		this.displayName = displayName;
		this.storedValue = storedValue;
	}

	public String getDisplayName() {
		return displayName;
	}
	
	public String getDisplayName(MessageSource messageSource, Locale l) {
		return messageSource.getMessage(displayName, null, l);
	}

	public static EventType getByStoredValue(String storedValue) {
		switch (storedValue) {
		case "V": return VOLUNTEER_AT_CP;
		default:
			return null;
		}
	}

	public static String getStoredValue(EventType e) {
		return e == null ? null : e.storedValue;
	}
}
