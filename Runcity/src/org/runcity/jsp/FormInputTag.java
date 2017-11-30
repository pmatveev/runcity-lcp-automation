package org.runcity.jsp;


import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.jstl.fmt.LocalizationContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.runcity.mvc.web.util.FormColumn;
import org.runcity.mvc.web.util.FormIdColumn;
import org.runcity.mvc.web.util.FormIdListColumn;
import org.runcity.mvc.web.util.FormListboxColumn;
import org.runcity.mvc.web.util.FormStringColumn;
import org.runcity.util.StringUtils;
import org.springframework.web.servlet.tags.form.ErrorsTag;
import org.springframework.web.servlet.tags.form.InputTag;
import org.springframework.web.servlet.tags.form.OptionTag;
import org.springframework.web.servlet.tags.form.PasswordInputTag;
import org.springframework.web.servlet.tags.form.SelectTag;
import org.springframework.web.servlet.tags.form.TagWriter;

@SuppressWarnings("serial")
public class FormInputTag extends TagSupport {
	private FormColumn<?> column;
	private Boolean status;
	private String autofocus;
	private LocalizationContext bundle;
	
	public void setColumn(FormColumn<?> column) {
		this.column = column;
	}
	
	public void setStatus(Boolean status) {
		this.status = status;
	}
	
	public void setAutofocus(String autofocus) {
		this.autofocus = autofocus;
	}
	
	public void setBundle(LocalizationContext bundle) {
		this.bundle = bundle;
	}

	@Override
	public int doStartTag() throws JspException {
		if (column instanceof FormIdColumn || column instanceof FormIdListColumn) {
			writeIdColumn(column);
			return SKIP_BODY;
		}
		
		if (column instanceof FormStringColumn) {
			writeStringColumn((FormStringColumn) column);
			return SKIP_BODY;
		}
		
		if (column instanceof FormListboxColumn<?>) {
			writeListboxColumn((FormListboxColumn<?>) column);
			return SKIP_BODY;
		}
		
		throw new JspException("Field type is not supported: " + column.getClass());
	}
	
	private String localize(String message) {
		return bundle.getResourceBundle().getString(message);
	}

	private void writeIdColumn(FormColumn<?> column) throws JspException {
		InputTag input = new InputTag();
		input.setPath(column.getName());
		input.setId(column.getHtmlId());
		input.setDynamicAttribute(null, "hidden", "hidden");
		
		if (column instanceof FormIdListColumn) {
			input.setDynamicAttribute(null, "format", "array");
		}
		
		input.setPageContext(pageContext);
		input.doStartTag();
		input.doEndTag();
	}
	
	private void writeLabel(TagWriter tagWriter, String label) throws JspException {
		tagWriter.startTag("label");
		tagWriter.writeAttribute("class", "control-label");
		tagWriter.writeAttribute("for", column.getHtmlId());
		tagWriter.appendValue(label);		
		tagWriter.endTag();
	}
	
	private void writeErrors(TagWriter tagWriter) throws JspException {
		ErrorsTag errors = new ErrorsTag();
		errors.setPath(column.getName());
		errors.setCssClass("help-block");
		
		errors.setPageContext(pageContext);
		errors.doStartTag();
		errors.doEndTag();
	}
	
	private void writeStringColumn(FormStringColumn column) throws JspException {
		String label = localize(column.getLabel());
		TagWriter tagWriter = new TagWriter(pageContext);
		
		tagWriter.startTag("div");	
		tagWriter.writeAttribute("class", status ? "form-group has-error" : "form-group");

		writeLabel(tagWriter, label);
		
		InputTag input;
		if (column.isPasswordValue()) {
			input = new PasswordInputTag();
		} else {
			input = new InputTag();
		}
		input.setPath(column.getName());
		input.setId(column.getHtmlId());
		input.setCssClass("form-control");
		input.setOnchange(column.getOnChange());
		input.setDynamicAttribute(null, "placeholder", label);
		input.setDynamicAttribute(null, "jschecks", column.getJsChecks());
		if (!StringUtils.isEmpty(autofocus)) {
			input.setDynamicAttribute(null, "autofocus", autofocus);
		}
		
		input.setPageContext(pageContext);
		input.doStartTag();
		input.doEndTag();
		
		writeErrors(tagWriter);
		
		tagWriter.endTag();
	}
	
	private void writeOption(SelectTag select, TagWriter tagWriter, String key, String value) throws JspException {
		OptionTag option = new OptionTag();
		option.setParent(select);
		option.setValue(key);
		option.setLabel(value);
		
		option.setPageContext(pageContext);
		option.doStartTag();
		option.doEndTag();
	}
	
	private void writeListboxColumn(FormListboxColumn<?> column) throws JspException {
		TagWriter tagWriter = new TagWriter(pageContext);
		
		tagWriter.startTag("div");	
		tagWriter.writeAttribute("class", status ? "form-group has-error" : "form-group");
		
		writeLabel(tagWriter, localize(column.getLabel()));
		
		tagWriter.startTag("br");
		tagWriter.endTag();
		
		SelectTag select = new SelectTag();
		select.setPath(column.getName());
		select.setId(column.getHtmlId());
		select.setCssClass("selectpicker");
		select.setMultiple(column.getMultipleOptions());
		select.setOnchange(column.getOnChange());
		select.setDynamicAttribute(null, "jschecks", column.getJsChecks());
		select.setDynamicAttribute(null, "data-live-search", column.getLiveSearch());
		select.setDynamicAttribute(null, "data-width", "100%");
		
		select.setPageContext(pageContext);
		select.doStartTag();
		
		if (!column.isMultiple()) {
			writeOption(select, tagWriter, "", "");
		}
		for (Map.Entry<String, String> entry : column.renderOptions(bundle.getResourceBundle())) {
			writeOption(select, tagWriter, entry.getKey(), entry.getValue());
		}
		
		select.doEndTag();
		
		writeErrors(tagWriter);
		
		tagWriter.endTag();
	}
}
