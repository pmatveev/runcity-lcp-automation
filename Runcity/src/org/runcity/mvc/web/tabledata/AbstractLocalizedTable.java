package org.runcity.mvc.web.tabledata;

import org.runcity.mvc.web.util.ColumnDefinition;
import org.runcity.util.DynamicLocaleList;
import org.springframework.context.MessageSource;

public abstract class AbstractLocalizedTable extends AbstractTable {
	protected DynamicLocaleList localeList;

	protected AbstractLocalizedTable(String id, String title, String ajaxData, DynamicLocaleList localeList) {
		super(id, title, ajaxData);
		this.localeList = localeList;
	}
	
	protected AbstractLocalizedTable(String id, String title, String ajaxData, MessageSource messageSource, DynamicLocaleList localeList) {
		this(id, title, ajaxData, localeList);
		super.setMessageSource(messageSource);
	}
	
	protected void addLocalizedColumn(String name, String label) {
		for (String l : localeList.keySet()) {
			this.columns.add(new ColumnDefinition(name + "." + localeList.get(l), label, localeList.get(l)));
		}
	}
}
