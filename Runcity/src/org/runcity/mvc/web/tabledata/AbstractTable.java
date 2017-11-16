package org.runcity.mvc.web.tabledata;

import java.util.LinkedList;
import java.util.List;

import org.runcity.mvc.rest.util.RestGetResponseBody;
import org.runcity.mvc.web.util.ColumnDefinition;
import org.springframework.context.MessageSource;

public class AbstractTable extends RestGetResponseBody {
	protected String id;
	protected List<ColumnDefinition> columns = new LinkedList<ColumnDefinition>();
	protected String ajaxData;
	protected String ajaxButtons;
	
	protected AbstractTable() {
		super();
	}
	
	protected AbstractTable(MessageSource messageSource) {
		super(messageSource);
	}
	
	public List<ColumnDefinition> getColumns() {
		return columns;
	}

	public String getAjaxData() {
		return ajaxData;
	}

	public String getAjaxButtons() {
		return ajaxButtons;
	}

	public String getId() {
		return id;
	}
}
