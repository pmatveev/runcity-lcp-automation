package org.runcity.jsp;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.jstl.fmt.LocalizationContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.springframework.web.servlet.tags.form.TagWriter;

@SuppressWarnings("serial")
public class KeyboardTag extends TagSupport {
	private TagWriter tagWriter;	
	private String bind;
	private String enable;
	private LocalizationContext bundle;
	private String[] rows;
	private final int multiplier = 100;
	private final int width = 90 * multiplier;

	public void setFor(String bind) {
		this.bind = bind;
	}

	public void setEnableFor(String enableFor) {
		this.enable = enableFor;
	}
	
	public void setBundle(LocalizationContext bundle) {
		this.bundle = bundle;
	}

	public void setRows(String rows) {
		this.rows = rows.split(",");
	}
	
	private void writeItem(String item, double itemWidth, double margin) throws JspException {				
		tagWriter.startTag("li");
		tagWriter.writeAttribute("style", "width:" + itemWidth + "%;margin-left:" + margin + "%;margin-right:" + margin + "%");
		
		switch (item) {
		case "backspace":
		case "enter":
			tagWriter.writeAttribute("class", item);
			break;
		default:
			tagWriter.writeAttribute("class", "symbol");
		}
		
		switch (item) {
		case "backspace":
			tagWriter.startTag("span");
			tagWriter.writeAttribute("class", "glyphicon glyphicon-chevron-left");
			tagWriter.endTag();
			break;
		case "enter":
			tagWriter.startTag("span");
			tagWriter.writeAttribute("class", "glyphicon glyphicon-ok");
			tagWriter.endTag();
			break;
		default:
			tagWriter.appendValue(item);
		}
		
		tagWriter.endTag();
	}
	
	private void writeRow(String row) throws JspException {
		tagWriter.startTag("div");
		tagWriter.writeAttribute("class", "row keyrow");
		String[] items = row.split(" ");
		double itemWidth = (double)(width / items.length) / multiplier;
		double margin = (double)((100 * multiplier - width) / (items.length * 2)) / multiplier;
		for (String item : items) {
			if (item.length() > 0) {
				writeItem(item, itemWidth, margin);
			}
		}
		tagWriter.endTag();
	}
	
	@Override
	public int doStartTag() throws JspException {
		tagWriter = new TagWriter(pageContext);
		tagWriter.startTag("div");
		tagWriter.writeAttribute("class", "keyboard");
		tagWriter.writeAttribute("for", bind);
		tagWriter.writeOptionalAttributeValue("enable", enable);
		
		tagWriter.startTag("ul");
		tagWriter.writeAttribute("class", "keys");
		for (String row : rows) {
			writeRow(bundle.getResourceBundle().getString(row));
		}
		tagWriter.endTag();
		
		tagWriter.endTag();
		return SKIP_BODY;
	}
}
