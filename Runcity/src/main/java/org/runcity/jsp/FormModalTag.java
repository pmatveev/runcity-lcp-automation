package org.runcity.jsp;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.jstl.fmt.LocalizationContext;

import org.runcity.mvc.web.formdata.AbstractForm;
import org.runcity.mvc.web.tabledata.AbstractTable;
import org.springframework.web.servlet.tags.UrlTag;
import org.springframework.web.servlet.tags.form.FormTag;
import org.springframework.web.servlet.tags.form.TagWriter;

@SuppressWarnings("serial")
public class FormModalTag extends FormTag {
	private AbstractForm form;
	private Boolean modal;
	private LocalizationContext bundle;
	private AbstractTable relatedTable;
	private TagWriter tagWriter;
	private String scroll;
	
	public void setForm(AbstractForm form) {
		this.form = form;
	}
	
	public void setModal(Boolean modal) {
		this.modal = modal;
	}
	
	public void setBundle(LocalizationContext bundle) {
		this.bundle = bundle;
	}
	
	public void setRelatedTable(AbstractTable relatedTable) {
		this.relatedTable = relatedTable;
	}
	
	public void setScroll(String scroll) {
		this.scroll = scroll;
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
		} else {
			pageContext.setAttribute(var, null);
		}
	}
	
	private boolean skipForm() {
		return form == null || Boolean.TRUE.equals(pageContext.getAttribute("written_" + form.getFormName()));
	}
	
	@Override
	protected int writeTagContent(TagWriter tagWriter) throws JspException {
		if (skipForm()) {
			return SKIP_BODY;
		}
		if (this.getDynamicAttributes() != null) {
			this.getDynamicAttributes().clear();
		}
		this.tagWriter = tagWriter;

		processUrl(form.getUrlOnSubmit(), form.getFormName() + "_formAction");
		if (modal) {
			processUrl(form.getUrlOnOpenAjax(), form.getFormName() + "_formFetchFrom");
			
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
			
			if (form.getTitle() != null) {
				tagWriter.startTag("h4");
				tagWriter.writeAttribute("class", "modal-title");
				tagWriter.appendValue(localize(form.getTitle()));
				tagWriter.endTag();
			}
			
			tagWriter.endTag();
		} else {
			if (form.getTitle() != null) {
				tagWriter.startTag("h1");
				tagWriter.appendValue(localize(form.getTitle()));
				tagWriter.endTag();
			}
			
			tagWriter.startTag("div");
			tagWriter.writeAttribute("id", "form_" + form.getHtmlId());
		}
		
		setMethod("POST");
		setModelAttribute(form.getFormName());
		setDynamicAttribute(null, "scroll", scroll);
		
		Object action = pageContext.getAttribute(form.getFormName() + "_formAction");
		if (action != null) {
			setAction(action.toString());
		} else {
			setAction(null);
		}
		setId(form.getHtmlId());
		setOnsubmit(form.getOnSubmit());
		if (modal) {
			Object val = pageContext.getAttribute(form.getFormName() + "_formFetchFrom");
			if (val != null) {
				setDynamicAttribute(null, "fetchFrom", val.toString());
			}
			
			if (relatedTable != null) {
				setDynamicAttribute(null, "related-table", relatedTable.getHtmlId());
			}
		}

		return super.writeTagContent(tagWriter);
	}
	
	@Override
	public int doEndTag() throws JspException {
		if (skipForm()) {
			return EVAL_PAGE;
		}
		int result = super.doEndTag();
		if (modal) {
			tagWriter.endTag();
			tagWriter.endTag();
			tagWriter.endTag();
		} else {
			tagWriter.endTag();
		}
		pageContext.setAttribute("written_" + form.getFormName() + (modal ? "_modal" : ""), Boolean.TRUE);
		return result;
	}
}
