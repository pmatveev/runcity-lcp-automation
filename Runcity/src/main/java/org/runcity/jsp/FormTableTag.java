package org.runcity.jsp;

import java.text.MessageFormat;
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
	private Boolean caption = true;

	public void setTable(AbstractTable table) {
		this.table = table;
	}

	public void setBundle(LocalizationContext bundle) {
		this.bundle = bundle;
	}
	
	public void setCaption(Boolean caption) {
		this.caption = caption;
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

	@Override
	public int doStartTag() throws JspException {
		TagWriter tagWriter = new TagWriter(pageContext);

		// buttons div - to be destroyed by JS
		if (table.getButtons().size() > 0) {
			tagWriter.startTag("div");
			tagWriter.writeAttribute("style", "display: none;");
			tagWriter.writeAttribute("id", table.getHtmlId() + "_buttons");
	
			for (ButtonDefinition b : table.getButtons()) {
				tagWriter.startTag("button");
				tagWriter.writeOptionalAttributeValue("class", b.getClassName());
				tagWriter.writeOptionalAttributeValue("extend", b.getExtend());
				tagWriter.writeOptionalAttributeValue("jscheck", b.getJsCondition());
				if (b.getAction() == null || (!b.getAction().startsWith("ajax:") && !b.getAction().startsWith("link:"))) {
					tagWriter.writeOptionalAttributeValue("action", b.getAction());
				} else {
					// need to correct URL
					String[] action = b.getAction().split(":");
					if (b.getAction().startsWith("ajax:") && action.length > 2) {
						processUrl(action[2], "ajax");
						action[2] = (String) pageContext.getAttribute("ajax");
					}
					if (b.getAction().startsWith("link:") && action.length > 1) {
						processUrl(action[1], "link");
						action[1] = (String) pageContext.getAttribute("link");
					}
					tagWriter.writeOptionalAttributeValue("action", StringUtils.toString(Arrays.asList(action), ":", null));
				}			
				if (b.getConfirmation() != null) {
					tagWriter.writeOptionalAttributeValue("confirmation", localize(b.getConfirmation()));
				}
				tagWriter.appendValue(localize(b.getText()));
				tagWriter.endTag();
			}
	
			tagWriter.endTag();
		}
		
		// expand div - to be destroyed by JS
		if (table.getExtensions().size() > 0) {
			tagWriter.startTag("div");
			tagWriter.writeAttribute("style", "display: none;");
			tagWriter.writeAttribute("id", table.getHtmlId() + "_extension");
			
			tagWriter.startTag("table");
			tagWriter.writeAttribute("class", "dt-expand");
			tagWriter.writeAttribute("cellpadding", "5");
			tagWriter.writeAttribute("cellspacing", "0");
			tagWriter.writeAttribute("border", "0");
			tagWriter.writeAttribute("style", "padding-left:50px;");
			tagWriter.writeAttribute("width", "100%");
			
			for (ColumnDefinition e : table.getExtensions()) {
				tagWriter.startTag("tr");

				tagWriter.startTag("td");
				tagWriter.writeAttribute("class", "expandLabel");
				tagWriter.startTag("b");
				tagWriter.appendValue(MessageFormat.format(localize(e.getLabel()), e.getSubstitution()) + ":");
				tagWriter.endTag();
				tagWriter.endTag();
				
				tagWriter.startTag("td");
				tagWriter.writeAttribute("class", "dynamic");
				tagWriter.writeAttribute("mapping", e.getName());
				if (e.getFormat() != null) {
					tagWriter.writeAttribute("format", e.getFormat().name());
				}
				if (e.getImageUrl() != null) {
					processUrl(e.getImageUrl(), "image");
					tagWriter.writeOptionalAttributeValue("image-url", pageContext.getAttribute("image").toString());
				}
				tagWriter.endTag();
				
				tagWriter.endTag();
			}

			tagWriter.endTag();
			tagWriter.endTag();
		}

		if (caption) {
			tagWriter.startTag("h1");
			tagWriter.appendValue(MessageFormat.format(localize(table.getTitle()), table.getTitleArgs()));
			tagWriter.endTag();
		}
		
		processUrl(table.getAjaxData(), table.getId() + "_ajaxSource");
		processUrl(table.getExpandFrame(), table.getId() + "_frame");

		tagWriter.startTag("table");
		tagWriter.writeAttribute("id", table.getHtmlId());
		tagWriter.writeAttribute("class", "datatables table table-striped table-bordered");
		tagWriter.writeAttribute("ajaxSource", pageContext.getAttribute(table.getId() + "_ajaxSource").toString());
		tagWriter.writeAttribute("width", "100%");
		tagWriter.writeOptionalAttributeValue("prefix", table.getPrefix());
		if (pageContext.getAttribute(table.getId() + "_frame") != null) {
			tagWriter.writeAttribute("expand-frame", pageContext.getAttribute(table.getId() + "_frame").toString());
		}

		tagWriter.startTag("thead");
		tagWriter.startTag("tr");

		if (table.getExtensions().size() > 0 || table.getExpandFrame() != null) {
			tagWriter.startTag("th");
			tagWriter.writeOptionalAttributeValue("format", "expand");
			tagWriter.endTag();
		}
		
		for (ColumnDefinition cd : table.getColumns()) {
			tagWriter.startTag("th");
			tagWriter.writeAttribute("mapping", cd.getName());
			if (cd.getFormat() != null) {
				tagWriter.writeAttribute("format", cd.getFormat().name());
			}
			if (cd.getImageUrl() != null) {
				processUrl(cd.getImageUrl(), "image");
				tagWriter.writeOptionalAttributeValue("image-url", pageContext.getAttribute("image").toString());
			}
			if (cd.getSortIndex() != null) {
				tagWriter.writeOptionalAttributeValue("sort", cd.getSort());
				tagWriter.writeAttribute("sort-index", cd.getSortIndex().toString());
			}
			tagWriter.writeAttribute("td-visible", cd.isVisible().toString());
			if (cd.getLabel() != null) {
				tagWriter.appendValue(MessageFormat.format(localize(cd.getLabel()), cd.getSubstitution()));
			}
			tagWriter.endTag();
		}

		tagWriter.endTag();
		tagWriter.endTag();
		tagWriter.endTag();

		return SKIP_BODY;
	}
}
