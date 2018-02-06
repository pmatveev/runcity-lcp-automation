package org.runcity.mvc.web;

import org.apache.log4j.Logger;
import org.runcity.db.entity.Token;
import org.runcity.db.service.ConsumerService;
import org.runcity.mvc.validator.FormValidator;
import org.runcity.mvc.web.formdata.ChangePasswordByTokenForm;
import org.runcity.mvc.web.formdata.ConsumerRegisterForm;
import org.runcity.mvc.web.formdata.PasswordRecoveryForm;
import org.runcity.util.DynamicLocaleList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ConsumerController {
	private static final Logger logger = Logger.getLogger(ConsumerController.class);

	@Autowired
	private FormValidator validator;

	@Autowired
	private ConsumerService consumerService;

	@Autowired
	private DynamicLocaleList localeList;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.setValidator(validator);
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Model model) {
		logger.info("GET /");
		return "redirect:/login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String showLoginForm(Model model, @RequestParam(required = false) String state) {
		logger.info("GET /login");
		if (isAlreadyAuthenticated()) {
			return "redirect:/home";
		}

		if ("error".equals(state)) {
			model.addAttribute("error", "login.invalidPwd");
		}

		if ("errToken".equals(state)) {
			model.addAttribute("error", "login.invalidPwdResetToken");			
		}
		
		PasswordRecoveryForm form = new PasswordRecoveryForm(localeList);
		model.addAttribute(form.getFormName(), form);
		ConsumerRegisterForm form2 = new ConsumerRegisterForm(localeList);
		model.addAttribute(form2.getFormName(), form2);
		
		return "common/login";
	}

	private boolean isAlreadyAuthenticated() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		logger.debug("\tchecking authentication");
		if (authentication == null) {
			logger.debug("\t\tnot found");
			return false;
		}

		logger.debug("\t\tfound " + authentication.getClass().getSimpleName());

		return authentication instanceof RememberMeAuthenticationToken
				|| authentication instanceof UsernamePasswordAuthenticationToken;
	}

	@RequestMapping(value = "/recoverPassword", method = RequestMethod.GET)
	public String showRecoveryForm(Model model, @RequestParam(required = false) String token, @RequestParam(required = false) String check) {
		logger.info("GET /recoverPassword");
		logger.debug("\ttoken=" + token);
		logger.debug("\tcheck=" + check);

		Token t = consumerService.getPasswordResetToken(token, check);
		if (t == null) {
			return "redirect:/login?state=errToken";
		}
		
		ChangePasswordByTokenForm form = new ChangePasswordByTokenForm(localeList);
		form.setToken(token);
		form.setCheck(check);
		model.addAttribute(form.getFormName(), form);
		return "common/recoverPassword";
	}
}
