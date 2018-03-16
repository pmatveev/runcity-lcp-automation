package org.runcity.db.entity.enumeration;

import java.util.Locale;

import org.springframework.context.MessageSource;

public enum ControlPointType {
	START("controlPointType.start", "S"), 
	FINISH("controlPointType.finish", "F"), 
	REGULAR("controlPointType.regular", "R"), 
	STAGE_END("controlPointType.stageEnd", "E"), 
	BONUS("controlPointType.bonus", "B");

	private String displayName;
	private String storedValue;

	private ControlPointType(String displayName, String storedValue) {
		this.displayName = displayName;
		this.storedValue = storedValue;
	}

	public static String getDisplayName(ControlPointType data) {
		return data == null ? null : data.displayName;
	}
	
	public static String getDisplayName(ControlPointType data, MessageSource messageSource, Locale l) {
		return messageSource.getMessage(getDisplayName(data), null, l);
	}

	public static ControlPointType getByStoredValue(String storedValue) {
		switch (storedValue) {
		case "S": return START;
		case "F": return FINISH;
		case "R": return REGULAR;
		case "E": return STAGE_END;
		case "B": return BONUS;
		default:
			return null;
		}
	}

	public static String getStoredValue(ControlPointType e) {
		return e == null ? null : e.storedValue;
	}
}
