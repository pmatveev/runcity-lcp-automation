package org.runcity.mvc.web;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ExceptionHandlerController {
	private static final Logger logger = Logger.getLogger(ExceptionHandlerController.class);
	
	public ModelAndView handleException(Exception e) {
		logger.fatal("Controller exception", e);
		
		ModelAndView model = new ModelAndView("exception/exception");
		model.addObject("errMsg", e.getMessage());

		return model;
	}
}
