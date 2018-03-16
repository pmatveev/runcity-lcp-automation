package org.runcity.mvc.web;

import java.util.List;

import org.runcity.db.entity.Category;
import org.runcity.db.entity.ControlPoint;
import org.runcity.db.entity.Game;
import org.runcity.db.entity.Volunteer;
import org.runcity.db.service.CategoryService;
import org.runcity.db.service.ControlPointService;
import org.runcity.db.service.GameService;
import org.runcity.db.service.VolunteerService;
import org.runcity.mvc.web.formdata.TeamProcessForm;
import org.runcity.mvc.web.tabledata.CategoryTable;
import org.runcity.mvc.web.tabledata.ConsumerTable;
import org.runcity.mvc.web.tabledata.ControlPointTable;
import org.runcity.mvc.web.tabledata.CoordinatorControlPointTable;
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
	private ControlPointService controlPointService;
	
	@Autowired 
	private CategoryService categoryService;
	
	@Autowired 
	private VolunteerService volunteerService;

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String redirectHome() {
		return "redirect:/secure/home";
	}

	@RequestMapping(value = "/secure/home", method = RequestMethod.GET)
	public String home(Model model) {
		SecureUserDetails user = SecureUserDetails.getCurrentUser();
		
		if (user.getAuthorities().contains(SecureUserDetails.VOLUNTEER_ROLE)) {
			return "redirect:/secure/volunteer";
		}
		
		if (user.getAuthorities().contains(SecureUserDetails.ADMIN_ROLE)) {
			return "redirect:/secure/games";
		}
		
		return "/exception/noRole";
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
		Game g = gameService.selectById(gameId, Game.SelectMode.NONE);
		if (g == null) {
			return "exception/invalidUrl";
		}
		ControlPointTable table = new ControlPointTable(messageSource, localeList, g);
		table.processModel(model);
		
		return "/secure/controlPoints";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/secure/games/{gameId}/categories", method = RequestMethod.GET)
	public String categoriesByGame(Model model, @PathVariable Long gameId) {
		Game g = gameService.selectById(gameId, Game.SelectMode.NONE);
		if (g == null) {
			return "exception/invalidUrl";
		}
		
		RouteTable table = new RouteTable(messageSource, localeList, g);
		table.processModel(model);
		
		return "/secure/routes";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/secure/categories/{categoryId}/games", method = RequestMethod.GET)
	public String gamesByCategory(Model model, @PathVariable Long categoryId) {
		Category c = categoryService.selectById(categoryId, Category.SelectMode.NONE);
		if (c == null) {
			return "exception/invalidUrl";
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

	@Secured("ROLE_VOLUNTEER")
	@RequestMapping(value = "/secure/volunteer", method = RequestMethod.GET)
	public String volunteer(Model model) {
		String username = SecureUserDetails.getCurrentUser().getUsername();
		List<Volunteer> volunteer = volunteerService.getUpcomingVolunteers(username, Volunteer.SelectMode.WITH_ACTIVE);
		List<Volunteer> coordinator = volunteerService.getUpcomingCoordinations(username, Volunteer.SelectMode.NONE);
		
		model.addAttribute("volunteerData", volunteer);
		model.addAttribute("coordinatorData", coordinator);
		
		return "/secure/volunteer";
	}

	@Secured("ROLE_VOLUNTEER")
	@RequestMapping(value = "/secure/volunteerDetails/{cpId}", method = RequestMethod.GET)
	public String volunteerDetails(Model model, @PathVariable Long cpId) {
		ControlPoint cp = controlPointService.selectById(cpId, ControlPoint.SelectMode.FOR_VOLUNTEER);
		
		if (cp == null) {
			return "exception/invalidUrl";			
		}

		String username = SecureUserDetails.getCurrentUser().getUsername();
		Volunteer v = volunteerService.selectByControlPointAndUsername(cp, username, Volunteer.SelectMode.WITH_ACTIVE);
		
		if (v == null) {
			return "exception/forbidden";		
		}

		v.setControlPoint(cp);
		model.addAttribute("volunteer", v);
		
		if (v.getActive()) {
			TeamProcessForm form = new TeamProcessForm();
			form.setVolunteerId(v.getId());
			model.addAttribute(form.getFormName(), form);
		}
		
		return "secure/volunteerDetails";	
	}
	
	@Secured("ROLE_VOLUNTEER")
	@RequestMapping(value = "/secure/current", method = RequestMethod.GET)
	public String currentVolunteerDetails(Model model) {
		SecureUserDetails user = SecureUserDetails.getCurrentUser();
		
		if (user == null) {
			return "exception/forbidden";
		}
		
		Volunteer v = volunteerService.getCurrentByUsername(user.getUsername());
		
		if (v == null || v.getControlPoint() == null) {
			// exception
			user.setCurrent(null);
			return "redirect:/secure/volunteer";
		}
		
		// just for a case
		user.setCurrent(v);
		return "redirect:/secure/volunteerDetails/" + v.getControlPoint().getId();
	}
	
	@Secured("ROLE_VOLUNTEER")
	@RequestMapping(value = "/secure/coordinatorDetails/{gameId}", method = RequestMethod.GET)
	public String coordinatorDetails(Model model, @PathVariable Long gameId) {
		Game g = gameService.selectById(gameId, Game.SelectMode.NONE);
		
		if (g == null) {
			return "exception/invalidUrl";			
		}

		String username = SecureUserDetails.getCurrentUser().getUsername();
		Volunteer v = volunteerService.selectCoordinatorByUsername(g, username, Volunteer.SelectMode.NONE);
		
		if (v == null) {
			return "exception/forbidden";		
		}
		
		model.addAttribute("volunteer", v);
		
		CoordinatorControlPointTable controlPoints = new CoordinatorControlPointTable(messageSource, localeList, g);
		controlPoints.processModel(model);
		
		return "secure/coordinatorDetails";	
	}
}
