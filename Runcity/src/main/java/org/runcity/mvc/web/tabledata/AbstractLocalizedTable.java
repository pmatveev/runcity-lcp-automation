package org.runcity.mvc.web.tabledata;

import org.runcity.mvc.web.util.ColumnDefinition;
import org.runcity.secure.SecureUserDetails;
import org.runcity.util.DynamicLocaleList;
import org.runcity.util.StringUtils;
import org.springframework.context.MessageSource;

public abstract class AbstractLocalizedTable extends AbstractTable {
	protected DynamicLocaleList localeList;

	protected AbstractLocalizedTable(String id, String title, String ajaxData, MessageSource messageSource, DynamicLocaleList localeList) {
		super(id, title, ajaxData, messageSource);
		this.localeList = localeList;
	}
	
	protected void addLocalizedColumn(String name, String label, boolean sort) {
		String userLocale = SecureUserDetails.getLocaleCurrent();
		for (String l : localeList.locales()) {
			ColumnDefinition c = new ColumnDefinition(name + "." + l, null, label, messageSource.getMessage("locale." + l, null, locale)); 
			
			if (sort && StringUtils.isEqual(userLocale, l)) {
				c.setSort("asc");
			}
			
			this.columns.add(c);
		}
	}
}
