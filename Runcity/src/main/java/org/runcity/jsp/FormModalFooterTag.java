package org.runcity.jsp;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.jstl.fmt.LocalizationContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class FormModalFooterTag extends SimpleTagSupport {
	private Boolean modal;
	private LocalizationContext bundle;
	private StringWriter sw = new StringWriter();

	public void setModal(Boolean modal) {
		this.modal = modal;
	}
	
	public void setBundle(LocalizationContext bundle) {
		this.bundle = bundle;
	}

	private String localize(String message) {
		return bundle.getResourceBundle().getString(message);
	}

	@Override
	public void doTag() throws JspException, IOException {
		JspWriter out = getJspContext().getOut();

		if (modal) {
			out.println("<div class='modal-footer'>");
		}

		out.println("<div class='form-group'>");
		
		if (modal) {
			out.print("<label class='create-another-wrapper'>");
			out.print("<input type='checkbox' class='ignore-value create-another'/>");
			out.print(localize("common.createAnother"));			
			out.print("</label>");
		}
		
		out.print("<button type='submit' class='btn btn-primary'>");
		out.print(localize("common.submitForm"));
		out.println("</button>");
		
		if (modal) {
			out.print("<button type='button' class='btn btn-link' data-dismiss='modal'>");
			out.print(localize("common.closeModal"));
			out.println("</button>");			
		}
		
		if (getJspBody() != null) {
			getJspBody().invoke(sw);
			out.println(sw.toString());
		}

		out.println("</div>");
		
		if (modal) {
			out.println("</div>");
		}
	}
}
