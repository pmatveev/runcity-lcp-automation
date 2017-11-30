package org.runcity.jsp;

import java.util.Arrays;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.jstl.fmt.LocalizationContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.runcity.mvc.web.tabledata.AbstractTable;
import org.runcity.mvc.web.util.ButtonDefinition;
import org.runcity.mvc.web.util.ColumnDefinition;
import org.runcity.util.StringUtils;
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
		} else {
			pageContext.setAttribute(var, null);
		}
	}

	@Override
	public int doStartTag() throws JspException {
		TagWriter tagWriter = new TagWriter(pageContext);

		// buttons div - to be destroyed by JS
		tagWriter.startTag("div");
		tagWriter.writeAttribute("style", "display: none;");
		tagWriter.writeAttribute("id", table.getId() + "_buttons");

		for (ButtonDefinition b : table.getButtons()) {
			tagWriter.startTag("button");
			tagWriter.writeOptionalAttributeValue("class", b.getClassName());
			tagWriter.writeOptionalAttributeValue("extend", b.getExtend());
			if (b.getAction() == null || !b.getAction().startsWith("ajax")) {
				tagWriter.writeOptionalAttributeValue("action", b.getAction());
			} else {
				// need to correct URL
				String[] action = b.getAction().split(":");
				if (action.length > 2) {
					processUrl(action[2], "ajaxButton");
					action[2] = (String) pageContext.getAttribute("ajaxButton");
					tagWriter.writeOptionalAttributeValue("action", StringUtils.toString(Arrays.asList(action), ":", null));
				}
			}			
			if (b.getConfirmation() != null) {
				tagWriter.writeOptionalAttributeValue("confirmation", bundle.getResourceBundle().getString(b.getConfirmation()));
			}
			tagWriter.appendValue(bundle.getResourceBundle().getString(b.getText()));
			tagWriter.endTag();
		}

		tagWriter.endTag();

		processUrl(table.getAjaxData(), table.getId() + "_ajaxSource");

		tagWriter.startTag("table");
		tagWriter.writeAttribute("id", table.getId());
		tagWriter.writeAttribute("class", "datatables table table-striped table-bordered");
		tagWriter.writeAttribute("ajaxSource", pageContext.getAttribute(table.getId() + "_ajaxSource").toString());

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
