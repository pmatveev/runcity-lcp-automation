package org.runcity.mvc.web.util;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.runcity.mvc.web.formdata.AbstractForm;

public class FormListboxTimezoneColumn extends FormListboxColumn<String> {
	@Override
	protected void initDictionary() {
		this.options = staticOptions;
		this.localized = false;
	}

	private static final Map<String, String> staticOptions = new HashMap<String, String>();

	static {
		for (String id : TimeZone.getAvailableIDs()) {
			staticOptions.put(id, timeZoneDisplay(id));
		}
	}

	private static String timeZoneDisplay(String id) {
		TimeZone tz = TimeZone.getTimeZone(id);

		long hours = TimeUnit.MILLISECONDS.toHours(tz.getRawOffset());
		long minutes = Math.abs(TimeUnit.MILLISECONDS.toMinutes(tz.getRawOffset()) - TimeUnit.HOURS.toMinutes(hours));
		String result = "";
		if (hours > 0) {
			result = String.format("(GMT+%02d:%02d) %s", hours, minutes, tz.getID());
		} else if (hours < 0) {
			result = String.format("(GMT%03d:%02d) %s", hours, minutes, tz.getID());
		} else {
			result = String.format("(GMT %02d:%02d) %s", hours, minutes, tz.getID());
		}

		return result;
	}

	public FormListboxTimezoneColumn() {
	}

	public FormListboxTimezoneColumn(AbstractForm form, ColumnDefinition definition) {
		super(form, definition, false);
	}
}
