package org.runcity.mvc.web.util;

import org.runcity.db.entity.enumeration.ControlPointType;
import org.runcity.mvc.web.formdata.AbstractForm;

public class FormListboxControlPointTypeColumn extends FormListboxColumn<String> {
	@Override
	protected void initDictionary() {
		for (ControlPointType ct : ControlPointType.values()) {
			options.put(ControlPointType.getStoredValue(ct), ct.getDisplayName());
		}
	}

	public FormListboxControlPointTypeColumn() {
	}

	public FormListboxControlPointTypeColumn(AbstractForm form, ColumnDefinition definition) {
		super(form, definition, false);
	}
}