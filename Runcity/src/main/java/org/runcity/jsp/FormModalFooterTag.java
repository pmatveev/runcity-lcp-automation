package org.runcity.jsp;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class FormModalFooterTag extends SimpleTagSupport {
	private Boolean modal;
	private StringWriter sw = new StringWriter();

	public void setModal(Boolean modal) {
		this.modal = modal;
	}

	@Override
	public void doTag() throws JspException, IOException {
		JspWriter out = getJspContext().getOut();

		if (modal) {
			out.println("<div class='modal-footer'>");
		}

		getJspBody().invoke(sw);
		getJspContext().getOut().println(sw.toString());

		if (modal) {
			out.println("</div>");
		}
	}
	/*
	 * @Override public int doStartTag() throws JspException { this.tagWriter =
	 * new TagWriter(pageContext); if (modal) { tagWriter.startTag("div");
	 * tagWriter.writeAttribute("class", "modal-footer"); tagWriter.endTag(); }
	 * return EVAL_BODY_INCLUDE; }
	 * 
	 * 
	 * 
	 * @Override public int doEndTag() throws JspException { if (modal) { //
	 * tagWriter.endTag(); } return EVAL_PAGE; }
	 */
}
