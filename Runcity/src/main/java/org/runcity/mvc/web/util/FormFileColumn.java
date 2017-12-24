package org.runcity.mvc.web.util;

import java.util.ArrayList;
import java.util.List;

import org.runcity.mvc.web.formdata.AbstractForm;
import org.runcity.util.FileCache;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.Errors;

public class FormFileColumn extends FormColumn<String> {
	protected String fileExtn;
	protected String previewUrl;
	protected List<String> previewParms;
	protected String uploadUrl;
	protected byte[] byteValue;
	
	public FormFileColumn(AbstractForm form, ColumnDefinition definition, String uploadUrl, String previewUrl, FormColumn<?>[] previewParms, String fileExtn) {
		super(form, definition);
		this.uploadUrl = uploadUrl;
		this.previewUrl = previewUrl;
		this.previewParms = new ArrayList<String>(previewParms.length);
		for (FormColumn<?> pp : previewParms) {
			this.previewParms.add(pp.getHtmlId());
		}
		this.fileExtn = fileExtn;
	}
	
	public FormFileColumn(AbstractForm form, ColumnDefinition definition, String uploadUrl, String previewUrl, FormColumn<?>[] previewParms, String fileExtn, String value) {
		this(form, definition, uploadUrl, previewUrl, previewParms, fileExtn);
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
	
	public String getPreviewUrl() {
		return previewUrl;
	}

	public List<String> getPreviewParms() {
		return previewParms;
	}

	public byte[] getByteValue() {
		return byteValue;
	}
}
