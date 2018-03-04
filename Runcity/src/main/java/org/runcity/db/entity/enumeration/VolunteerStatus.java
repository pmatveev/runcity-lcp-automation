package org.runcity.db.entity.enumeration;

import java.util.Locale;

import org.springframework.context.MessageSource;

public enum VolunteerStatus {
	INPUT("volunteerStatus.input", "I"), 
	READY("volunteerStatus.ready", "R"),
	ON_SITE("volunteerStatus.onsite", "S"), 
	DONE("volunteerStatus.done", "D");

	private String displayName;
	private String storedValue;

	private VolunteerStatus(String displayName, String storedValue) {
		this.displayName = displayName;
		this.storedValue = storedValue;
	}

	public String getDisplayName() {
		return displayName;
	}

	public String getDisplayName(MessageSource messageSource, Locale l) {
		return messageSource.getMessage(displayName, null, l);
	}

	public static VolunteerStatus getByStoredValue(String storedValue) {
		switch (storedValue) {

		default:
			return null;
		}
	}

	public static String getStoredValue(VolunteerStatus e) {
		return e == null ? null : e.storedValue;
	}
}
