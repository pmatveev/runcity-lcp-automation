package org.runcity.mvc.web.util;

import org.apache.log4j.Logger;
import org.runcity.mvc.web.formdata.AbstractForm;

public class FormFileColumn extends FormColumn<Byte[]> {
	// TODO value type !!!
	protected String fileExtn;
	
	private static final Logger logger = Logger.getLogger(FormFileColumn.class);
	
	public FormFileColumn(AbstractForm form, ColumnDefinition definition, String fileExtn) {
		super(form, definition);
		this.fileExtn = fileExtn;
	}
	
	public FormFileColumn(AbstractForm form, ColumnDefinition definition, String fileExtn, Byte[] value) {
		this(form, definition, fileExtn);
		this.value = value;
	}
	
	// TODO
	
	public String getFileExtn() {
		return fileExtn;
	}
}
