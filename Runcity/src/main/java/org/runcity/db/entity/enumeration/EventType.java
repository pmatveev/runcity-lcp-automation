package org.runcity.db.entity.enumeration;

import java.util.Locale;

import org.springframework.context.MessageSource;

public enum EventType {
	VOLUNTEER_AT_CP("eventType.volunteerCP", "V"),
	TEAM_CP("eventType.teamPass", "C"),
	TEAM_COORD("eventType.teamCoord", "R");

	private String displayName;
	private String storedValue;

	private EventType(String displayName, String storedValue) {
		this.displayName = displayName;
		this.storedValue = storedValue;
	}

	public static String getDisplayName(EventType data) {
		return data == null ? null : data.displayName;
	}
	
	public static String getDisplayName(EventType data, MessageSource messageSource, Locale l) {
		return messageSource.getMessage(getDisplayName(data), null, l);
	}

	public static EventType getByStoredValue(String storedValue) {
		switch (storedValue) {
		case "C": return TEAM_CP;
		case "V": return VOLUNTEER_AT_CP;
		case "R": return TEAM_COORD;
		default:
			return null;
		}
	}

	public static String getStoredValue(EventType e) {
		return e == null ? null : e.storedValue;
	}
}
