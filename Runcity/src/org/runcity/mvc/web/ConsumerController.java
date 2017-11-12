package org.runcity.mvc.web;

import org.apache.log4j.Logger;
import org.runcity.db.service.ConsumerService;
import org.runcity.exception.DBException;
import org.runcity.mvc.validator.FormValidator;
import org.runcity.mvc.web.formdata.ConsumerEditForm;
import org.runcity.mvc.web.formdata.ConsumerRegisterForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ConsumerController {
	private static final Logger logger = Logger.getLogger(ConsumerController.class);

	@Autowired
	private ExceptionHandlerController exceptionHandler;

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
		logger.info("GET /");
		return "redirect:/login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String showLoginForm(Model model, @RequestParam(required = false) String error) {
		logger.info("GET /login");
		if (isAlreadyAuthenticated()) {
			return "redirect:/home";
		}

		if (error != null) {
			model.addAttribute("error", "login.invalidPwd");
		}
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

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String showRegisterForm(Model model) {
		logger.info("GET /register");
		ConsumerRegisterForm form = new ConsumerRegisterForm();
		model.addAttribute(form.getFormName(), form);
		return "common/register";
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public String doRegister(@ModelAttribute("consumerRegisterForm") @Validated ConsumerRegisterForm form, BindingResult result,
			Model model, final RedirectAttributes redirectAttributes) {
		logger.info("POST /register");
		if (result.hasErrors()) {
			logger.info("\tvalidation error");
			return "common/register";
		}

		try {
			consumerService.register(form.getUsername(), form.getPassword(), form.getCredentials(), form.getEmail());
		} catch (DBException e) {
			result.reject("common.db.fail");
			logger.error("DB exception", e);
			return "common/register";
		}
		return "redirect:/login";
	}

	@ExceptionHandler(Exception.class)
	public ModelAndView handleException(Exception e) {
		return exceptionHandler.handleException(e);
	}
	
	//TODO
	@RequestMapping(value="/secure/consumerEdit", method = RequestMethod.POST) 
	@Secured("ADMIN_ROLE")
	public String doConsumerEdit(@ModelAttribute("consumerEditForm") @Validated ConsumerEditForm form, BindingResult result, Model model, final RedirectAttributes redirectAttributes) {	
		return "/secure/home";
	}
}
