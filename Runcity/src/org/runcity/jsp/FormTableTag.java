package org.runcity.jsp;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.jstl.fmt.LocalizationContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.runcity.mvc.web.tabledata.AbstractTable;
import org.runcity.mvc.web.util.ColumnDefinition;
import org.springframework.web.servlet.tags.UrlTag;
import org.springframework.web.servlet.tags.form.TagWriter;

@SuppressWarnings("serial")
public class FormTableTag extends TagSupport {
	private AbstractTable table;
	private LocalizationContext bundle;

	public void setTable(AbstractTable table) {
		this.table = table;
	}

	public void setBundle(LocalizationContext bundle) {
		this.bundle = bundle;
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
	public int doStartTag() throws JspException {
		TagWriter tagWriter = new TagWriter(pageContext);

		processUrl(table.getAjaxData(), "ajaxSource");
		processUrl(table.getAjaxButtons(), "ajaxButtonSource");

		tagWriter.startTag("table");
		tagWriter.writeAttribute("id", table.getId());
		tagWriter.writeAttribute("class", "datatables table table-striped table-bordered");
		tagWriter.writeAttribute("ajaxSource", pageContext.getAttribute("ajaxSource").toString());
		tagWriter.writeOptionalAttributeValue("ajaxButtonSource",
				pageContext.getAttribute("ajaxButtonSource").toString());

		tagWriter.startTag("thead");
		tagWriter.startTag("rt");

		for (ColumnDefinition cd : table.getColumns()) {
			tagWriter.startTag("th");
			tagWriter.writeAttribute("mapping", cd.getName());
			if (cd.getLabel() != null) {
				tagWriter.appendValue(bundle.getResourceBundle().getString(cd.getLabel()));
			}
			tagWriter.endTag();
		}

		tagWriter.endTag();
		tagWriter.endTag();
		tagWriter.endTag();

		return SKIP_BODY;
	}
}
