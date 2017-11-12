package org.runcity.mvc.web.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.runcity.mvc.web.formdata.AbstractForm;
import org.runcity.secure.SecureUserRole;

public class FormListboxUserRoleColumn extends FormListboxColumn<List<String>> {
	private static Map<String, String> options = new HashMap<String, String>();
	
	static {
		options.put(SecureUserRole.ADMIN.name(), "role.admin");
		options.put(SecureUserRole.VOLUNTEER.name(), "role.volunteer");
	}
	
	public FormListboxUserRoleColumn(AbstractForm form, ColumnDefinition definition, boolean required) {
		super(form, definition, true, required, options.size());
		value = new LinkedList<String>();
	}

	public FormListboxUserRoleColumn(AbstractForm form, ColumnDefinition definition, boolean required, String value) {
		this(form, definition, required);
		this.value.add(value);
	}

	public FormListboxUserRoleColumn(AbstractForm form, ColumnDefinition definition, boolean required, List<String> value) {
		super(form, definition, true, required, options.size(), value);
		if (this.value == null) {
			value = new LinkedList<String>();			
		}
	}
	
	public void addValue(String value) {
		if (!this.value.contains(value)) {
			this.value.add(value);
		}
	}

	@Override
	public Map<String, String> getOptions() {
		return options;
	}

}
