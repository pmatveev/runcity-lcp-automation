package org.runcity.mvc.web;

import org.runcity.db.entity.Category;
import org.runcity.db.entity.Game;
import org.runcity.db.service.CategoryService;
import org.runcity.db.service.GameService;
import org.runcity.mvc.web.tabledata.CategoryTable;
import org.runcity.mvc.web.tabledata.ConsumerTable;
import org.runcity.mvc.web.tabledata.ControlPointTable;
import org.runcity.mvc.web.tabledata.RouteTable;
import org.runcity.secure.SecureUserDetails;
import org.runcity.mvc.web.tabledata.GameTable;
import org.runcity.util.DynamicLocaleList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MenuController {
	@Autowired
	protected MessageSource messageSource;
	
	@Autowired
	private DynamicLocaleList localeList;
	
	@Autowired 
	private GameService gameService;
	
	@Autowired 
	private CategoryService categoryService;

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String redirectHome() {
		return "redirect:/secure/home";
	}

	@RequestMapping(value = "/secure/home", method = RequestMethod.GET)
	public String home(Model model) {
		SecureUserDetails user = SecureUserDetails.getCurrent();
		
		if (user.getAuthorities().contains(SecureUserDetails.ADMIN_ROLE)) {
			return "redirect:/secure/games";
		}
		
		return "/secure/home";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/secure/categories", method = RequestMethod.GET)
	public String categories(Model model) {
		CategoryTable table = new CategoryTable(messageSource, localeList);
		table.processModel(model);
		
		return "/secure/categories";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/secure/games", method = RequestMethod.GET)
	public String games(Model model) {
		GameTable table = new GameTable(messageSource, localeList);
		table.processModel(model);
		
		return "/secure/games";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/secure/games/{gameId}/controlPoints", method = RequestMethod.GET)
	public String controlPointsByGame(Model model, @PathVariable Long gameId) {
		Game g = gameService.selectById(gameId, false);
		if (g == null) {
			throw new RuntimeException();
		}
		ControlPointTable table = new ControlPointTable(messageSource, localeList, g);
		table.processModel(model);
		
		return "/secure/controlPoints";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/secure/games/{gameId}/categories", method = RequestMethod.GET)
	public String categoriesByGame(Model model, @PathVariable Long gameId) {
		Game g = gameService.selectById(gameId, false);
		if (g == null) {
			throw new RuntimeException();
		}
		
		RouteTable table = new RouteTable(messageSource, localeList, g);
		table.processModel(model);
		
		return "/secure/routes";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/secure/categories/{categoryId}/games", method = RequestMethod.GET)
	public String gamesByCategory(Model model, @PathVariable Long categoryId) {
		Category c = categoryService.selectById(categoryId, false);
		if (c == null) {
			throw new RuntimeException();
		}
		
		RouteTable table = new RouteTable(messageSource, localeList, c);
		table.processModel(model);
		
		return "/secure/routes";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/secure/users", method = RequestMethod.GET)
	public String users(Model model) {
		ConsumerTable table = new ConsumerTable(messageSource, localeList);
		table.processModel(model);
		
		return "/secure/users";
	}
}
