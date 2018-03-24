package org.runcity.db.entity.enumeration;

import java.util.Locale;

import org.springframework.context.MessageSource;

public enum EventType {
	VOLUNTEER_AT_CP("eventType.volunteerCP", "V"),
	TEAM_CP("eventType.teamPass", "C"),
	TEAM_COORD("eventType.teamCoord", "R"),
	TEAM_CP_EXCEPTION("eventType.teamPassException", "c"),
	TEAM_COORD_EXCEPTION("eventType.teamCoordException", "r"),
	TEAM_ACTIVE("eventType.teamActive", "T");

	private String displayName;
	private String storedValue;

	private EventType(String displayName, String storedValue) {
		this.displayName = displayName;
		this.storedValue = storedValue;
	}

	public static String getDisplayName(EventType data) {
		if (data == null) {
			return "";
		}
		return data == null ? null : data.displayName;
	}
	
	public static String getDisplayName(EventType data, MessageSource messageSource, Locale l) {
		if (data == null) {
			return "";
		}
		return messageSource.getMessage(getDisplayName(data), null, l);
	}

	public static EventType getByStoredValue(String storedValue) {
		if (storedValue == null) {
			return null;
		}
		switch (storedValue) {
		case "c": return TEAM_CP_EXCEPTION;
		case "C": return TEAM_CP;
		case "V": return VOLUNTEER_AT_CP;
		case "r": return TEAM_COORD_EXCEPTION;
		case "R": return TEAM_COORD;
		case "T": return TEAM_ACTIVE;
		default:
			return null;
		}
	}

	public static String getStoredValue(EventType e) {
		return e == null ? null : e.storedValue;
	}
	
	public EventType getException() {
		switch (this) {
		case TEAM_COORD:
			return TEAM_COORD_EXCEPTION;
		case TEAM_CP:
			return TEAM_CP_EXCEPTION;
		default:
			return this;
		}
	}
}
