package org.runcity.mvc.web.tabledata;

import java.util.LinkedList;
import java.util.List;

import org.runcity.mvc.rest.util.RestGetResponseBody;
import org.runcity.mvc.web.formdata.AbstractForm;
import org.runcity.mvc.web.util.ButtonDefinition;
import org.runcity.mvc.web.util.ColumnDefinition;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;

public abstract class AbstractTable extends RestGetResponseBody {
	protected String id;
	protected List<ColumnDefinition> columns = new LinkedList<ColumnDefinition>();
	protected String ajaxData;
	protected List<ButtonDefinition> buttons = new LinkedList<ButtonDefinition>();
	protected List<AbstractForm> relatedForms = new LinkedList<AbstractForm>();
	
	protected AbstractTable() {
		super();
	}
	
	protected AbstractTable(MessageSource messageSource) {
		super(messageSource);
	}
	
	public List<ColumnDefinition> getColumns() {
		return columns;
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
