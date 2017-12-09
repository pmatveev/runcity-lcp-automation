package org.runcity.mvc.web.util;

import java.util.LinkedList;
import java.util.List;

import org.runcity.mvc.web.formdata.AbstractForm;
import org.runcity.secure.SecureUserRole;

public class FormListboxUserRoleColumn extends FormListboxColumn<List<String>> {
	@Override
	protected void initDictionary() {
		options.put(SecureUserRole.ADMIN.name(), "role.admin");
		options.put(SecureUserRole.VOLUNTEER.name(), "role.volunteer");
	}

	public FormListboxUserRoleColumn() {
	}

	public FormListboxUserRoleColumn(AbstractForm form, ColumnDefinition definition, boolean required) {
		super(form, definition, true, required);
		this.value = new LinkedList<String>();
	}

	public FormListboxUserRoleColumn(AbstractForm form, ColumnDefinition definition, boolean required, String value) {
		this(form, definition, required);
		this.value.add(value);
	}

	public FormListboxUserRoleColumn(AbstractForm form, ColumnDefinition definition, boolean required,
			List<String> value) {
		super(form, definition, true, required, value);
		if (this.value == null) {
			this.value = new LinkedList<String>();
		}
	}

	public void addValue(String value) {
		if (!this.value.contains(value)) {
			this.value.add(value);
		}
	}
}