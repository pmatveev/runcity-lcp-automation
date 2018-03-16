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

	public static String getDisplayName(EventStatus data) {
		return data == null ? null : data.displayName;
	}
	
	public static String getDisplayName(EventStatus data, MessageSource messageSource, Locale l) {
		return messageSource.getMessage(getDisplayName(data), null, l);
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
