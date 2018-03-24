package org.runcity.mvc.web.util;

import org.runcity.db.entity.enumeration.TeamStatus;
import org.runcity.mvc.web.formdata.AbstractForm;

public class FormListboxTeamStatusColumn extends FormListboxColumn<String> {
	@Override
	protected void initDictionary() {
		for (TeamStatus ts: TeamStatus.values()) {
			options.put(TeamStatus.getStoredValue(ts), TeamStatus.getDisplayName(ts));
		}
	}

	public FormListboxTeamStatusColumn() {
	}

	public FormListboxTeamStatusColumn(AbstractForm form, ColumnDefinition definition) {
		super(form, definition, false);
	}
}