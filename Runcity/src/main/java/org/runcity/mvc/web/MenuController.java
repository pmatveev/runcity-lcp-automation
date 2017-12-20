package org.runcity.mvc.web;

import java.util.List;

import org.runcity.db.entity.ControlPoint;
import org.runcity.db.service.ControlPointService;
import org.runcity.mvc.web.formdata.ControlPointCreateEditByGameForm;
import org.runcity.mvc.web.formdata.GameCreateEditForm;
import org.runcity.mvc.web.tabledata.CategoryTable;
import org.runcity.mvc.web.tabledata.ConsumerTable;
import org.runcity.mvc.web.tabledata.GameTable;
import org.runcity.util.DynamicLocaleList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MenuController {
	@Autowired
	protected MessageSource messageSource;
	
	@Autowired
	private DynamicLocaleList localeList;
	
	@Autowired 
	private ControlPointService controlPointService;
	
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String redirectHome() {
		return "redirect:/secure/home";
	}
	
	@RequestMapping(value = "/secure/home", method = RequestMethod.GET)
	public String home(Model model) {
		ControlPointCreateEditByGameForm form = new ControlPointCreateEditByGameForm(localeList);
		model.addAttribute(form.getFormName(), form);
		return "/secure/home";
	}
	
	@RequestMapping(value = "/secure/categories", method = RequestMethod.GET)
	public String categories(Model model) {
		CategoryTable table = new CategoryTable("/api/v1/categoryTable", messageSource, localeList);
		table.processModel(model);
		
		return "/secure/categories";
	}
	
	@RequestMapping(value = "/secure/games", method = RequestMethod.GET)
	public String games(Model model) {
		GameTable table = new GameTable("/api/v1/gameTable", messageSource, localeList);
		table.processModel(model);
		
		return "/secure/games";
	}
	
	@RequestMapping(value = "/secure/users", method = RequestMethod.GET)
	public String users(Model model) {
		ConsumerTable table = new ConsumerTable("/api/v1/consumerTable", messageSource, localeList);
		table.processModel(model);
		
		return "/secure/users";
	}
}
