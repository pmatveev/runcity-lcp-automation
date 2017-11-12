package org.runcity.mvc.web;

import org.runcity.mvc.web.formdata.ConsumerEditForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MenuController {
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String redirectHome() {
		return "redirect:/secure/home";
	}
	
	@RequestMapping(value = "/secure/home", method = RequestMethod.GET)
	public String home(Model model) {
		ConsumerEditForm form = new ConsumerEditForm();
		model.addAttribute(form.getFormName(), form);
		return "/secure/home";
	}
}
