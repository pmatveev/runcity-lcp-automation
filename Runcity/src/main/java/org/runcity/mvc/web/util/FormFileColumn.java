package org.runcity.mvc.web.util;

import org.runcity.mvc.web.formdata.AbstractForm;
import org.runcity.util.FileCache;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.Errors;

public class FormFileColumn extends FormColumn<String> {
	protected String fileExtn;
	protected String uploadUrl;
	protected byte[] byteValue;
	
	public FormFileColumn(AbstractForm form, ColumnDefinition definition, String fileExtn) {
		super(form, definition);
		this.fileExtn = fileExtn;
		this.uploadUrl = "/api/v1/uploadImage";
	}
	
	public FormFileColumn(AbstractForm form, ColumnDefinition definition, String fileExtn, String value) {
		this(form, definition, fileExtn);
		this.value = value;
	}
	
	@Override
	public void validate(ApplicationContext context, Errors errors) {
		super.validate(context, errors);
		
		FileCache cache = context.getBean(FileCache.class);
		byteValue = cache.get(value);
	}
	
	public String getFileExtn() {
		return fileExtn;
	}
	
	public String getUploadUrl() {
		return uploadUrl;
	}
	
	public byte[] getByteValue() {
		return byteValue;
	}
}
