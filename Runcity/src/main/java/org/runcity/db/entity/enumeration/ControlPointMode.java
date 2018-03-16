package org.runcity.db.entity.enumeration;

import java.util.Locale;

import org.runcity.util.StringUtils;
import org.springframework.context.MessageSource;

public enum ControlPointMode {
	ONLINE("controlPointMode.online", "Y", "label-success"), 
	OFFLINE("controlPointMode.offline", "N", "label-danger");

	private String displayName;
	private String storedValue;
	private String cls;

	private ControlPointMode(String displayName, String storedValue, String cls) {
		this.displayName = displayName;
		this.storedValue = storedValue;
		this.cls = cls;
	}

	public static String getDisplayName(ControlPointMode data) {
		return data == null ? null : data.displayName;
	}
	
	public static String getDisplayName(ControlPointMode data, MessageSource messageSource, Locale l) {
		return messageSource.getMessage(getDisplayName(data), null, l);
	}
	
	public static String getDisplayBadge(ControlPointMode data, MessageSource messageSource, Locale l) {
		return "<span class='label " + data == null ? "" : data.cls + "'>" + StringUtils.xss(getDisplayName(data, messageSource, l)) + "</span>";
	}

	public static ControlPointMode getByStoredValue(String storedValue) {
		switch (storedValue) {
		case "Y": return ONLINE;
		case "N": return OFFLINE;
		default:
			return null;
		}
	}

	public static String getStoredValue(ControlPointMode e) {
		return e == null ? null : e.storedValue;
	}
}
