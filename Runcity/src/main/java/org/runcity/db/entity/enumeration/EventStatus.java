package org.runcity.db.entity.enumeration;

import java.util.Locale;

import org.springframework.context.MessageSource;

public enum EventStatus {
	CLOSED("eventStatus.closed", "C"),
	POSTED("eventStatus.posted", "P");

	private String displayName;
	private String storedValue;

	private EventStatus(String displayName, String storedValue) {
		this.displayName = displayName;
		this.storedValue = storedValue;
	}

	public String getDisplayName() {
		return displayName;
	}
	
	public String getDisplayName(MessageSource messageSource, Locale l) {
		return messageSource.getMessage(displayName, null, l);
	}

	public static EventStatus getByStoredValue(String storedValue) {
		switch (storedValue) {
		case "C": return CLOSED;
		case "P": return POSTED;
		default:
			return null;
		}
	}

	public static String getStoredValue(EventStatus e) {
		return e == null ? null : e.storedValue;
	}
}
