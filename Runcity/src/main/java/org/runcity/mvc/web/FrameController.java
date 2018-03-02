package org.runcity.mvc.web;

import org.runcity.db.entity.Consumer;
import org.runcity.db.entity.ControlPoint;
import org.runcity.db.entity.Game;
import org.runcity.db.entity.Route;
import org.runcity.db.service.ConsumerService;
import org.runcity.db.service.ControlPointService;
import org.runcity.db.service.GameService;
import org.runcity.db.service.RouteService;
import org.runcity.mvc.web.tabledata.RouteItemTable;
import org.runcity.mvc.web.tabledata.TeamTable;
import org.runcity.mvc.web.tabledata.VolunteerTable;
import org.runcity.util.DynamicLocaleList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FrameController {
	@Autowired
	protected MessageSource messageSource;
	
	@Autowired
	private DynamicLocaleList localeList;
	
	@Autowired
	private ConsumerService consumerService;
	
	@Autowired
	private ControlPointService controlPointService;
	
	@Autowired
	private GameService gameService;
	
	@Autowired
	private RouteService routeService;

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "secure/iframe/route/{routeId}", method = RequestMethod.GET)
	public String routeDetails(Model model, @PathVariable Long routeId, @RequestParam(required = true) String referrer) {
		Route r = routeService.selectById(routeId, true);
		
		if (r == null) {
			return "exception/invalidUrlSub";
		}
	
		RouteItemTable routeItemTable = new RouteItemTable(messageSource, localeList, r);
		routeItemTable.processModel(model, referrer);
		
		TeamTable teamTable = new TeamTable(messageSource, localeList, r);
		teamTable.processModel(model, referrer);
		
		model.addAttribute("prefix", referrer);
		return "/sub/routeDetails";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/secure/iframe/game/{gameId}", method = RequestMethod.GET)
	public String gameDetails(Model model, @PathVariable Long gameId, @RequestParam(required = true) String referrer) {
		Game g = gameService.selectById(gameId, false);
		
		if (g == null) {
			return "exception/invalidUrlSub";
		}

		VolunteerTable coordinatorsTable = VolunteerTable.initCoordinatorsByGame(messageSource, localeList, g);
		VolunteerTable volunteersTable = VolunteerTable.initVolunteersByGame(messageSource, localeList, g);;

		coordinatorsTable.processModel(model, referrer);
		volunteersTable.processModel(model, referrer);

		model.addAttribute("prefix", referrer);
		return "/sub/gameDetails";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/secure/iframe/controlPoint/{controlPointId}", method = RequestMethod.GET)
	public String controlPointDetails(Model model, @PathVariable Long controlPointId, @RequestParam(required = true) String referrer) {
		ControlPoint cp = controlPointService.selectById(controlPointId, false);
		
		if (cp == null) {
			return "exception/invalidUrlSub";
		}

		VolunteerTable table = new VolunteerTable(messageSource, localeList, cp);
		table.processModel(model, referrer);

		model.addAttribute("prefix", referrer);
		return "/sub/controlPointDetails";
	}

	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/secure/iframe/consumer/{consumerId}", method = RequestMethod.GET)
	public String consumerDetails(Model model, @PathVariable Long consumerId, @RequestParam(required = true) String referrer) {
		Consumer c = consumerService.selectById(consumerId, false);
		
		if (c == null) {
			return "exception/invalidUrlSub";
		}

		VolunteerTable coordinatorsTable = VolunteerTable.initCoordinatorsByConsumer(messageSource, localeList, c);
		VolunteerTable volunteersTable = VolunteerTable.initVolunteersByConsumer(messageSource, localeList, c);

		coordinatorsTable.processModel(model, referrer);
		volunteersTable.processModel(model, referrer);

		model.addAttribute("prefix", referrer);
		return "/sub/consumerDetails";
	}
}
