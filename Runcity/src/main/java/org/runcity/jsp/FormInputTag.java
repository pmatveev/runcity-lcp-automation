package org.runcity.jsp;

import java.text.MessageFormat;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.jstl.fmt.LocalizationContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.runcity.mvc.config.SpringRootConfig;
import org.runcity.mvc.web.util.FormColorPickerColumn;
import org.runcity.mvc.web.util.FormColumn;
import org.runcity.mvc.web.util.FormDateColumn;
import org.runcity.mvc.web.util.FormDddwColumn;
import org.runcity.mvc.web.util.FormFileColumn;
import org.runcity.mvc.web.util.FormIdColumn;
import org.runcity.mvc.web.util.FormListIdColumn;
import org.runcity.mvc.web.util.FormListboxColumn;
import org.runcity.mvc.web.util.FormLocalizedStringColumn;
import org.runcity.mvc.web.util.FormNumberColumn;
import org.runcity.mvc.web.util.FormStringColumn;
import org.runcity.util.StringUtils;
import org.springframework.web.servlet.tags.UrlTag;
import org.springframework.web.servlet.tags.form.AbstractHtmlInputElementTag;
import org.springframework.web.servlet.tags.form.ErrorsTag;
import org.springframework.web.servlet.tags.form.InputTag;
import org.springframework.web.servlet.tags.form.OptionTag;
import org.springframework.web.servlet.tags.form.PasswordInputTag;
import org.springframework.web.servlet.tags.form.SelectTag;
import org.springframework.web.servlet.tags.form.TagWriter;
import org.springframework.web.servlet.tags.form.TextareaTag;

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
		if (column instanceof FormIdColumn || column instanceof FormListIdColumn) {
			writeIdColumn(column);
			return SKIP_BODY;
		}

		if (column instanceof FormNumberColumn) {
			writeNumberColumn((FormNumberColumn) column);
			return SKIP_BODY;
		}

		if (column instanceof FormStringColumn) {
			writeStringColumn((FormStringColumn) column);
			return SKIP_BODY;
		}

		if (column instanceof FormLocalizedStringColumn) {
			writeLocalizedStringColumn((FormLocalizedStringColumn) column);
			return SKIP_BODY;
		}

		if (column instanceof FormListboxColumn<?>) {
			writeListboxColumn((FormListboxColumn<?>) column);
			return SKIP_BODY;
		}

		if (column instanceof FormDddwColumn<?>) {
			writeDddwColumn((FormDddwColumn<?>) column);
			return SKIP_BODY;
		}

		if (column instanceof FormDateColumn) {
			writeDateColumn((FormDateColumn) column);
			return SKIP_BODY;
		}

		if (column instanceof FormFileColumn) {
			writeFileColumn((FormFileColumn) column);
			return SKIP_BODY;
		}

		throw new JspException("Field type is not supported: " + column.getClass());
	}

	private String localize(String message) {
		return bundle.getResourceBundle().getString(message);
	}

	private void processUrl(String url, String var) throws JspException {
		if (url != null) {
			UrlTag urlTag = new UrlTag();
			urlTag.setValue(url);
			urlTag.setVar(var);

			urlTag.setPageContext(pageContext);
			urlTag.doStartTag();
			urlTag.doEndTag();
		} else {
			pageContext.setAttribute(var, null);
		}
	}

	private void writeIdColumn(FormColumn<?> column) throws JspException {
		InputTag input = new InputTag();
		input.setPath(column.getName());
		input.setId(column.getHtmlId());
		input.setDynamicAttribute(null, "hidden", "hidden");

		if (column.getValue() != null) {
			input.setDynamicAttribute(null, "default", column.getValue());
		}
		
		if (column instanceof FormListIdColumn) {
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

	private void writeNumberColumn(FormNumberColumn column) throws JspException {
		String label = localize(column.getLabel());
		TagWriter tagWriter = new TagWriter(pageContext);

		tagWriter.startTag("div");
		tagWriter.writeAttribute("class", status ? "form-group has-error" : "form-group");
		writeLabel(tagWriter, label);

		InputTag input = new InputTag();

		input.setPath(column.getName());
		input.setId(column.getHtmlId());

		if (column.getValue() != null) {
			input.setDynamicAttribute(null, "default", column.getValue());
		}
		
		input.setCssClass("form-control");
		input.setOnchange(column.getOnChange());
		input.setDynamicAttribute(null, "placeholder", label);
		input.setDynamicAttribute(null, "jschecks", column.getJsChecks());
		input.setDynamicAttribute(null, "show-if", column.getShowCondition());
		input.setDynamicAttribute(null, "type", "number");

		if (!StringUtils.isEmpty(autofocus)) {
			input.setDynamicAttribute(null, "autofocus", autofocus);
		}

		input.setPageContext(pageContext);
		input.doStartTag();
		input.doEndTag();

		writeErrors(tagWriter);

		tagWriter.endTag();
	}	
	
	private void writeStringColumn(FormStringColumn column) throws JspException {
		String label = column.isHidden() ? null : localize(column.getLabel());
		TagWriter tagWriter = new TagWriter(pageContext);

		tagWriter.startTag("div");
		tagWriter.writeAttribute("class", status ? "form-group has-error" : "form-group");
		if (!column.isHidden()) {	
			writeLabel(tagWriter, label);
	
			if (column instanceof FormColorPickerColumn) {
				tagWriter.startTag("div");
				tagWriter.writeAttribute("class", "input-group colorpicker-component");
				tagWriter.appendValue("");
			}
		} else {
			tagWriter.appendValue("");
		}

		AbstractHtmlInputElementTag input;
		if (column.isPasswordValue()) {
			input = new PasswordInputTag();
		} else if (column.isLongValue()) {
			input = new TextareaTag();
		} else {
			input = new InputTag();
		}
		if (column instanceof FormColorPickerColumn) {
			input.setDynamicAttribute(null, "display-type", "colorpicker");
		}
		input.setPath(column.getName());
		input.setId(column.getHtmlId());

		if (column.getValue() != null) {
			input.setDynamicAttribute(null, "default", column.getValue());
		}
		
		if (column.isHidden()) {
			input.setDynamicAttribute(null, "hidden", "hidden");
		} else {
			input.setCssClass("form-control");
			input.setOnchange(column.getOnChange());
			input.setDynamicAttribute(null, "placeholder", label);
			input.setDynamicAttribute(null, "jschecks", column.getJsChecks());
			input.setDynamicAttribute(null, "show-if", column.getShowCondition());
	
			if (!StringUtils.isEmpty(autofocus)) {
				input.setDynamicAttribute(null, "autofocus", autofocus);
			}
		}
		input.setPageContext(pageContext);
		input.doStartTag();
		input.doEndTag();

		if (column instanceof FormColorPickerColumn && !column.isHidden()) {
			tagWriter.startTag("span");
			tagWriter.writeAttribute("class", "input-group-addon");
			tagWriter.startTag("i");
			tagWriter.appendValue("");
			tagWriter.endTag();
			tagWriter.endTag();
			tagWriter.endTag();
		}

		writeErrors(tagWriter);

		tagWriter.endTag();
	}

	private void writeLocalizedStringColumn(FormLocalizedStringColumn column) throws JspException {
		String label = localize(column.getLabel());
		TagWriter tagWriter = new TagWriter(pageContext);

		tagWriter.startTag("div");
		tagWriter.writeAttribute("class", status ? "form-group has-error" : "form-group");

		writeLabel(tagWriter, localize(column.getGroupLabel()));

		boolean autofocusAllowed = true;
		for (String l : column.keySet()) {
			String localeDisplay = localize("locale." + l);
			tagWriter.startTag("div");
			tagWriter.writeAttribute("class", "input-group input-group-locale");

			tagWriter.startTag("span");
			tagWriter.writeAttribute("class", "input-group-addon input-group-add-locale");
			tagWriter.writeAttribute("id", "span_" + column.getHtmlId() + l);
			tagWriter.appendValue(localeDisplay);
			tagWriter.endTag();

			AbstractHtmlInputElementTag input = null;
			if (column.isLongValue()) {
				input = new TextareaTag();
			} else {
				input = new InputTag();
			}
			input.setPath(column.getName() + "['" + l + "']");
			input.setId(column.getHtmlId() + l);
			input.setCssClass("form-control");
			input.setDynamicAttribute(null, "format", "langObj");
			input.setDynamicAttribute(null, "jschecks", column.getJsChecks());
			input.setDynamicAttribute(null, "show-if", column.getShowCondition());
			input.setOnchange(column.getOnChange(l));

			String placeholder = MessageFormat.format(label, localeDisplay);
			input.setDynamicAttribute(null, "placeholder", placeholder);
			input.setDynamicAttribute(null, "aria-label", placeholder);
			input.setDynamicAttribute(null, "aria-describedby", "span_" + column.getHtmlId() + l);

			if (!StringUtils.isEmpty(autofocus) && autofocusAllowed) {
				input.setDynamicAttribute(null, "autofocus", autofocus);
				autofocusAllowed = false;
			}

			input.setPageContext(pageContext);
			input.doStartTag();
			input.doEndTag();

			tagWriter.endTag();
		}
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
		select.setDynamicAttribute(null, "show-if", column.getShowCondition());
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

	private void writeDddwColumn(FormDddwColumn<?> column) throws JspException {
		TagWriter tagWriter = new TagWriter(pageContext);

		tagWriter.startTag("div");
		tagWriter.writeAttribute("class", status ? "form-group has-error" : "form-group");

		writeLabel(tagWriter, localize(column.getLabel()));

		tagWriter.startTag("br");
		tagWriter.endTag();

		processUrl(column.getAjaxSource(), "selectAjax");
		processUrl(column.getInitSource(), "initAjax");

		SelectTag select = new SelectTag();
		select.setPath(column.getName());
		select.setId(column.getHtmlId());
		select.setCssClass("selectpicker ajax-sourced");
		select.setMultiple(column.getMultipleOptions());
		select.setOnchange(column.getOnChange());
		select.setDynamicAttribute(null, "jschecks", column.getJsChecks());
		select.setDynamicAttribute(null, "show-if", column.getShowCondition());
		select.setDynamicAttribute(null, "data-width", "100%");
		select.setDynamicAttribute(null, "data-live-search", "true");
		if (column.isForceRefresh()) {
			select.setDynamicAttribute(null, "force-refresh", "true");
		}

		Object ajax = pageContext.getAttribute("selectAjax");
		if (ajax != null) {
			select.setDynamicAttribute(null, "ajax-data", ajax);
			
			if (column.getAjaxParms() != null) {
				select.setDynamicAttribute(null, "ajax-parms", StringUtils.toString(column.getAjaxParms(), ":", ""));
			}
		}

		Object initAjax = pageContext.getAttribute("initAjax");
		if (initAjax != null) {
			select.setDynamicAttribute(null, "ajax-data-init", initAjax);
			
			if (column.getInitParms() != null) {
				select.setDynamicAttribute(null, "init-parms", StringUtils.toString(column.getInitParms(), ":", ""));
			}
		}

		select.setPageContext(pageContext);
		select.doStartTag();

		select.doEndTag();

		writeErrors(tagWriter);

		tagWriter.endTag();
	}

	private void writeDateColumn(FormDateColumn column) throws JspException {
		String label = localize(column.getLabel());
		TagWriter tagWriter = new TagWriter(pageContext);

		tagWriter.startTag("div");
		tagWriter.writeAttribute("class", status ? "form-group has-error" : "form-group");

		writeLabel(tagWriter, label);

		// "display" input
		tagWriter.startTag("div");
		tagWriter.writeAttribute("data-link-field", column.getHtmlId());
		if (column.isTimeValue()) {
			tagWriter.writeAttribute("class", "input-group date datetimepicker-component");
			tagWriter.writeAttribute("data-date-format", localize("common.dateTimeFormat"));
			tagWriter.writeAttribute("data-link-format", SpringRootConfig.DATE_TIME_FORMAT_JS);
		} else {
			tagWriter.writeAttribute("class", "input-group date datepicker-component");
			tagWriter.writeAttribute("data-date-format", localize("common.dateFormat"));
			tagWriter.writeAttribute("data-link-format", SpringRootConfig.DATE_FORMAT_JS);
		}
		if (Boolean.TRUE.equals(pageContext.getAttribute("modal"))) {
			tagWriter.writeAttribute("data-date-container", "#modal_" + column.getFormHtmlId());
		} else {
			tagWriter.writeAttribute("data-date-container", "html");
		}

		tagWriter.startTag("input");
		tagWriter.writeAttribute("class", "form-control ignore-value");
		tagWriter.writeAttribute("type", "text");
		tagWriter.writeAttribute("placeholder", label);
		if (!StringUtils.isEmpty(autofocus)) {
			tagWriter.writeAttribute("autofocus", autofocus);
		}
		tagWriter.endTag();

		tagWriter.startTag("span");
		tagWriter.writeAttribute("class", "input-group-addon");
		tagWriter.startTag("span");
		tagWriter.writeAttribute("class", "glyphicon glyphicon-remove");
		tagWriter.appendValue("");
		tagWriter.endTag();
		tagWriter.endTag();

		tagWriter.startTag("span");
		tagWriter.writeAttribute("class", "input-group-addon");
		tagWriter.startTag("span");
		tagWriter.writeAttribute("class", "glyphicon glyphicon-calendar");
		tagWriter.appendValue("");
		tagWriter.endTag();
		tagWriter.endTag();

		tagWriter.endTag();

		// actual input - contains data
		InputTag input = new InputTag();

		input.setPath(column.getName());
		input.setId(column.getHtmlId());
		input.setOnchange(column.getOnChange());
		input.setDynamicAttribute(null, "jschecks", column.getJsChecks());
		input.setDynamicAttribute(null, "show-if", column.getShowCondition());
		input.setDynamicAttribute(null, "type", "hidden");
		
		if (column.isTimeValue()) {
			input.setDynamicAttribute(null, "display-type", "datetimepicker");
		} else {
			input.setDynamicAttribute(null, "display-type", "datepicker");
		}
		if (column.getValue() != null) {
			input.setDynamicAttribute(null, "default", column.getValue());
		}

		input.setPageContext(pageContext);
		input.doStartTag();
		input.doEndTag();

		writeErrors(tagWriter);

		tagWriter.endTag();
	}	
	
	private void writeFileColumn(FormFileColumn column) throws JspException {
		String label = localize(column.getLabel());
		TagWriter tagWriter = new TagWriter(pageContext);

		tagWriter.startTag("div");
		tagWriter.writeAttribute("class", status ? "form-group has-error" : "form-group");
		tagWriter.appendValue("");

		processUrl(column.getUploadUrl(), "upload"); 
		processUrl(column.getPreviewUrl(), "preview"); 
		InputTag input = new InputTag();
		input.setPath(column.getName());
		input.setId(column.getHtmlId());
		input.setCssClass("form-control fileinput");
		input.setDynamicAttribute(null, "type", "file");
		input.setDynamicAttribute(null, "extensions", column.getFileExtn());
		input.setDynamicAttribute(null, "placeholder", label);
		input.setDynamicAttribute(null, "upload-to", pageContext.getAttribute("upload"));
		input.setDynamicAttribute(null, "initial", pageContext.getAttribute("preview"));
		input.setDynamicAttribute(null, "initial-parms", StringUtils.toString(column.getPreviewParms(), ":", null));
		input.setDynamicAttribute(null, "format", "file");
		input.setDynamicAttribute(null, "display-type", "filepicker");

		if (!StringUtils.isEmpty(autofocus)) {
			input.setDynamicAttribute(null, "autofocus", autofocus);
		}

		input.setPageContext(pageContext);
		input.doStartTag();
		input.doEndTag();

		writeErrors(tagWriter);

		tagWriter.endTag();
	}
}
