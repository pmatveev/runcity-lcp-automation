package org.runcity.mvc.web;

import org.runcity.db.entity.Game;
import org.runcity.db.service.GameService;
import org.runcity.mvc.web.tabledata.CategoryTable;
import org.runcity.mvc.web.tabledata.ConsumerTable;
import org.runcity.mvc.web.tabledata.ControlPointTable;
import org.runcity.mvc.web.tabledata.RouteTable;
import org.runcity.mvc.web.tabledata.GameTable;
import org.runcity.util.DynamicLocaleList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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
	
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String redirectHome() {
		return "redirect:/secure/home";
	}
	
	@RequestMapping(value = "/secure/home", method = RequestMethod.GET)
	public String home(Model model) {
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
	
	@RequestMapping(value = "/secure/games/{gameId}/controlPoints", method = RequestMethod.GET)
	public String controlPointsByGame(Model model, @PathVariable Long gameId) {
		Game g = gameService.selectById(gameId, false);
		if (g == null) {
			throw new RuntimeException();
		}
		ControlPointTable table = new ControlPointTable("/api/v1/controlPointsTable?gameId=" + g.getId(), messageSource, localeList, g);
		table.processModel(model);
		
		return "/secure/controlPoints";
	}
	
	@RequestMapping(value = "/secure/games/{gameId}/categories", method = RequestMethod.GET)
	public String categoriesByGame(Model model, @PathVariable Long gameId) {
		Game g = gameService.selectById(gameId, false);
		if (g == null) {
			throw new RuntimeException();
		}
		
		RouteTable table = new RouteTable("/api/v1/routeTable?gameId=" + g.getId(), messageSource, localeList, g);
		table.processModel(model);
		
		return "/secure/routes";
	}
	
	@RequestMapping(value = "/secure/users", method = RequestMethod.GET)
	public String users(Model model) {
		ConsumerTable table = new ConsumerTable("/api/v1/consumerTable", messageSource, localeList);
		table.processModel(model);
		
		return "/secure/users";
	}
}
