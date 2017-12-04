package org.runcity.mvc.web;

import org.runcity.mvc.web.formdata.ChangePasswordByPasswordForm;
import org.runcity.mvc.web.formdata.ConsumerSelfEditForm;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class AllController {
	@ModelAttribute
	public void addChangePassword(Model model) {
		if (!(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
			ChangePasswordByPasswordForm form = new ChangePasswordByPasswordForm();
			if (!model.containsAttribute(form.getFormName())) {
				model.addAttribute(form.getFormName(), form);
			}
		}
	}

	@ModelAttribute
	public void addSelfEdit(Model model) {
		if (!(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
			ConsumerSelfEditForm form = new ConsumerSelfEditForm();
			if (!model.containsAttribute(form.getFormName())) {
				model.addAttribute(form.getFormName(), form);
			}
		}
	}
}
