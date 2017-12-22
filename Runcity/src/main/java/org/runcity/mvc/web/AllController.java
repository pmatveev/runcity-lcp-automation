package org.runcity.mvc.web;

import org.runcity.mvc.web.formdata.ChangePasswordByPasswordForm;
import org.runcity.mvc.web.formdata.ConsumerSelfEditForm;
import org.runcity.util.DynamicLocaleList;
import org.runcity.util.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class AllController {
	@Autowired
	private DynamicLocaleList localeList;

	@Autowired 
	private Version version;
	
	@Autowired 
	private ExceptionHandlerController exceptionHandler;
	
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
			ConsumerSelfEditForm form = new ConsumerSelfEditForm(localeList);
			if (!model.containsAttribute(form.getFormName())) {
				model.addAttribute(form.getFormName(), form);
			}
		}
	}

	@ModelAttribute
	public void addVersionInfo(Model model) {
		if (!model.containsAttribute(version.getName())) {
			model.addAttribute(version.getName(), version);
		}
	}

	@ExceptionHandler(Exception.class)
	public ModelAndView handleException(Exception e) {
		return exceptionHandler.handleException(e);
	}
}
