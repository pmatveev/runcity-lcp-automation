package org.runcity.mvc.web.tabledata;

import org.runcity.mvc.web.util.ColumnDefinition;
import org.runcity.util.DynamicLocaleList;
import org.springframework.context.MessageSource;

public abstract class AbstractLocalizedTable extends AbstractTable {
	protected DynamicLocaleList localeList;

	protected AbstractLocalizedTable(String id, String title, String ajaxData, MessageSource messageSource, DynamicLocaleList localeList) {
		super(id, title, ajaxData, messageSource);
		this.localeList = localeList;
	}
	
	protected void addLocalizedColumn(String name, String label) {
		for (String l : localeList.locales()) {
			this.columns.add(new ColumnDefinition(name + "." + l, null, label, messageSource.getMessage("locale." + l, null, locale)));
		}
	}
}
