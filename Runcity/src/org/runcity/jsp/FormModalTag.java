package org.runcity.jsp;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.jstl.fmt.LocalizationContext;

import org.runcity.mvc.web.formdata.AbstractForm;
import org.springframework.web.servlet.tags.UrlTag;
import org.springframework.web.servlet.tags.form.FormTag;
import org.springframework.web.servlet.tags.form.TagWriter;

@SuppressWarnings("serial")
public class FormModalTag extends FormTag {
	private AbstractForm form;
	private Boolean modal;
	private LocalizationContext bundle;
	private TagWriter tagWriter;
	
	public void setForm(AbstractForm form) {
		this.form = form;
	}
	
	public void setModal(Boolean modal) {
		this.modal = modal;
	}
	
	public void setBundle(LocalizationContext bundle) {
		this.bundle = bundle;
	}
	
	public FormModalTag() {
		super();
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
		}
	}
	
	@Override
	protected int writeTagContent(TagWriter tagWriter) throws JspException {
		this.tagWriter = tagWriter;

		String onSubmit;
		if (modal) {
			processUrl(form.getUrlOnSubmitAjax(), "formAction");
			processUrl(form.getUrlOnOpenAjax(), "formFetchFrom");
			
			onSubmit = form.getOnModalSubmit();
			
			tagWriter.startTag("div");
			tagWriter.writeAttribute("id", "modal_" + form.getHtmlId());
			tagWriter.writeAttribute("class", "modal");
			tagWriter.writeAttribute("role", "dialog");
			
			tagWriter.startTag("div");
			tagWriter.writeAttribute("class", "modal-dialog");
			
			tagWriter.startTag("div");
			tagWriter.writeAttribute("class", "modal-content");
			
			tagWriter.startTag("div");
			tagWriter.writeAttribute("class", "modal-header");
			
			tagWriter.startTag("h4");
			tagWriter.writeAttribute("class", "modal-title");
			tagWriter.appendValue(localize(form.getTitle()));
			tagWriter.endTag();
			
			tagWriter.endTag();
		} else {
			processUrl(form.getUrlOnSubmit(), "formAction");

			onSubmit = form.getOnSubmit();
			
			tagWriter.startTag("h1");
			tagWriter.appendValue(localize(form.getTitle()));
			tagWriter.endTag();
		}
		
		setMethod("POST");
		setModelAttribute(form.getFormName());
		setAction(pageContext.getAttribute("formAction").toString());
		setId(form.getHtmlId());
		setOnsubmit(onSubmit);
		if (modal) {
			Object val = pageContext.getAttribute("formFetchFrom");
			if (val != null) {
				setDynamicAttribute(null, "fetchFrom", val.toString());
			}
		}

		return super.writeTagContent(tagWriter);
	}
	
	@Override
	public int doEndTag() throws JspException {
		int result = super.doEndTag();
		if (modal) {
			tagWriter.endTag();
			tagWriter.endTag();
			tagWriter.endTag();
		}
		return result;
	}
}
