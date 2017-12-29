package org.runcity.mvc.web.util;

import java.util.ArrayList;
import java.util.List;

import org.runcity.mvc.web.formdata.AbstractForm;

public class FormIdListColumn extends FormColumn<List<Long>> {
	public FormIdListColumn(AbstractForm form, ColumnDefinition definition) {
		super(form, definition);
		this.value = new ArrayList<Long>();
	}
}
