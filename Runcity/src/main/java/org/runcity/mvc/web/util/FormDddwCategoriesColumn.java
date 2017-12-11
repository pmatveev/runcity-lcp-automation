package org.runcity.mvc.web.util;

import java.util.ArrayList;
import java.util.List;

import org.runcity.mvc.web.formdata.AbstractForm;

public class FormDddwCategoriesColumn extends FormDddwColumn<List<Long>> {
	private FormDddwCategoriesColumn(AbstractForm form, ColumnDefinition definition, String ajaxSource,
			String[] ajaxParms, boolean required) {
		super(form, definition, ajaxSource, ajaxParms, true, required);
		this.value = new ArrayList<Long>();
	}

	public static FormDddwCategoriesColumn getAllByLocale(AbstractForm form, ColumnDefinition definition, String localeCol, boolean required) {
		return new FormDddwCategoriesColumn(form, definition, "/api/v1/dddw/categories?locale={0}", new String[] {localeCol}, required);
	}
}
