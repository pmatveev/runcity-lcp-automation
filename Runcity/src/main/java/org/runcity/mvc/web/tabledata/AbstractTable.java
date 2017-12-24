package org.runcity.mvc.web.tabledata;

import java.util.LinkedList;
import java.util.List;

import org.runcity.mvc.rest.util.RestGetResponseBody;
import org.runcity.mvc.web.formdata.AbstractForm;
import org.runcity.mvc.web.util.ButtonDefinition;
import org.runcity.mvc.web.util.ColumnDefinition;
import org.runcity.secure.SecureUserDetails;
import org.runcity.util.DynamicLocaleList;
import org.runcity.util.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;

public abstract class AbstractTable extends RestGetResponseBody {
	protected DynamicLocaleList localeList;
	protected String title;
	protected Object[] titleArgs;
	protected String id;
	protected List<ColumnDefinition> columns = new LinkedList<ColumnDefinition>();
	protected List<ColumnDefinition> extensions = new LinkedList<ColumnDefinition>();
	protected String ajaxData;
	protected List<ButtonDefinition> buttons = new LinkedList<ButtonDefinition>();
	protected List<AbstractForm> relatedForms = new LinkedList<AbstractForm>();
	
	protected AbstractTable(String id, String title, String ajaxData, MessageSource messageSource, DynamicLocaleList localeList) {
		super();
		this.id = id;
		this.title = title;
		this.ajaxData = ajaxData;
		this.localeList = localeList;
		super.setMessageSource(messageSource);
	}
	
	protected AbstractTable(String id, String title, String ajaxData, MessageSource messageSource, DynamicLocaleList localeList, Object ... titleArgs) {
		this(id, title, ajaxData, messageSource, localeList);
		this.titleArgs = titleArgs;
	}
	
	protected void addLocalizedColumn(List<ColumnDefinition> list, String name, String label) {
		addLocalizedColumn(list, name, label, null, 0);
	}
	
	protected void addLocalizedColumn(List<ColumnDefinition> list, String name, String label, String sortType, int sortOrder) {
		String userLocale = SecureUserDetails.getLocaleCurrent();
		boolean sort = !StringUtils.isEmpty(sortType);
		for (String l : localeList.locales()) {
			ColumnDefinition c = new ColumnDefinition(name + "." + l, null, label, messageSource.getMessage("locale." + l, null, locale)); 
			
			if (sort && StringUtils.isEqual(userLocale, l)) {
				c.setSort(sortType, sortOrder);
			}
			
			list.add(c);
		}
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Object[] getTitleArgs() {
		return titleArgs;
	}

	public void setTitleArgs(Object ... titleArgs) {
		this.titleArgs = titleArgs;
	}

	public List<ColumnDefinition> getColumns() {
		return columns;
	}

	public List<ColumnDefinition> getExtensions() {
		return extensions;
	}
	
	public List<ButtonDefinition> getButtons() {
		return buttons;
	}
	
	public List<AbstractForm> getRelatedForms() {
		return relatedForms;
	}

	public String getAjaxData() {
		return ajaxData;
	}

	public String getId() {
		return id;
	}
	
	public void processModel(Model model) {
		model.addAttribute(id, this);
		for (AbstractForm f : relatedForms) {
			model.addAttribute(f.getFormName(), f);
		}
	}
}
