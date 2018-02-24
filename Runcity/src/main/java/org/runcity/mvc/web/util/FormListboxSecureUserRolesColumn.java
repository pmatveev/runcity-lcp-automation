package org.runcity.mvc.web.util;

import java.util.LinkedList;
import java.util.List;

import org.runcity.db.entity.enumeration.SecureUserRole;
import org.runcity.mvc.web.formdata.AbstractForm;

public class FormListboxSecureUserRolesColumn extends FormListboxColumn<List<String>> {
	@Override
	protected void initDictionary() {
		for (SecureUserRole r : SecureUserRole.values()) {
			options.put(SecureUserRole.getStoredValue(r), r.getDisplayName());
		}
	}

	public FormListboxSecureUserRolesColumn() {
	}

	public FormListboxSecureUserRolesColumn(AbstractForm form, ColumnDefinition definition) {
		super(form, definition, true);
		this.value = new LinkedList<String>();
	}

	public void addValue(String value) {
		if (!this.value.contains(value)) {
			this.value.add(value);
		}
	}
}