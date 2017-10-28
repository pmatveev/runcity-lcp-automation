package org.runcity.mvc.web;

import org.runcity.db.service.ConsumerService;
import org.runcity.exception.DBException;
import org.runcity.mvc.validator.FormValidator;
import org.runcity.mvc.web.formdata.ConsumerForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ConsumerController {
	@Autowired
	private FormValidator validator;

	@Autowired
	private ConsumerService consumerService;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.setValidator(validator);
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Model model) {
		return "redirect:/login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String showLoginForm(Model model, @RequestParam(required = false) String error) {
		if (isRememberMeAuthenticated()) {
			return "redirect:/home";
		}

		if (error != null) {
			model.addAttribute("error", "login.invalidPwd");
		}
		return "common/login";
	}

	private boolean isRememberMeAuthenticated() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			return false;
		}

		return RememberMeAuthenticationToken.class.isAssignableFrom(authentication.getClass());
	}

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String showRegisterForm(Model model) {
		ConsumerForm form = new ConsumerForm();
		model.addAttribute(form.getFormName(), form);
		return "common/register";
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String doRegister(@ModelAttribute("consumerForm") @Validated ConsumerForm form, BindingResult result,
			Model model, final RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return "common/register";
		}

		try {
			consumerService.addNewConsumer(form);
		} catch (DBException e) {
			result.reject("mvc.db.fail");
			return "common/register";
		}
		return "redirect:/login";
	}
}
