package org.runcity.mvc.web;

import org.runcity.db.entity.Route;
import org.runcity.db.service.RouteService;
import org.runcity.mvc.web.tabledata.RouteItemTable;
import org.runcity.util.DynamicLocaleList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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
	private RouteService routeService;
	
	@RequestMapping(value = "secure/iframe/route/{routeId}", method = RequestMethod.GET)
	public String controlPointsByGame(Model model, @PathVariable Long routeId, @RequestParam(required = true) String referrer) {
		Route r = routeService.selectById(routeId, true);
		RouteItemTable table = new RouteItemTable("/api/v1/routeItemTable?routeId=" + routeId, messageSource, localeList, r);
		table.processModel(model);
		table.prefix(referrer);
		
		model.addAttribute("prefix", referrer);
		return "/sub/routeItems";
	}
}
