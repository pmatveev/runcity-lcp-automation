package org.runcity.mvc.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.runcity.util.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ExceptionHandlerController {
	private static final Logger logger = Logger.getLogger(ExceptionHandlerController.class);

	@RequestMapping(value = "errors", method = RequestMethod.GET)
	public String renderErrorPage(HttpServletRequest httpRequest) {
		switch (getErrorCode(httpRequest)) {
		case 404: {
			return "exception/invalidUrl";
		}
		}
		return "exception/exception";
	}

	private int getErrorCode(HttpServletRequest httpRequest) {
		return (Integer) httpRequest.getAttribute("javax.servlet.error.status_code");
	}

	@ExceptionHandler(Throwable.class)
	public ModelAndView handleException(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			Throwable e) {
		logger.fatal("Controller exception", e);

		if (StringUtils.isEqual(request.getContentType(), "application/x-www-form-urlencoded")) {
			ModelAndView model = new ModelAndView("exception/exception");
			model.addObject("errMsg", e.getMessage());

			return model;
		}

		return null;
	}

	@RequestMapping(value = "/403")
	public String getAccessDenied(Model model) {
		logger.info("GET 403");
		return "redirect:/login";
	}
}
