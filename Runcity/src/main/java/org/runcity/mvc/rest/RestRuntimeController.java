package org.runcity.mvc.rest;

import org.apache.log4j.Logger;
import org.runcity.db.entity.ControlPoint;
import org.runcity.db.entity.RouteItem;
import org.runcity.db.entity.Team;
import org.runcity.db.entity.Volunteer;
import org.runcity.db.service.ControlPointService;
import org.runcity.db.service.RouteService;
import org.runcity.db.service.TeamService;
import org.runcity.db.service.VolunteerService;
import org.runcity.exception.DBException;
import org.runcity.mvc.rest.util.RestGetInfoResponseBody;
import org.runcity.mvc.rest.util.RestGetResponseBody;
import org.runcity.mvc.rest.util.RestPostResponseBody;
import org.runcity.mvc.rest.util.Views;
import org.runcity.mvc.web.formdata.TeamProcessByVolunteerForm;
import org.runcity.mvc.web.tabledata.VolunteerTeamTable;
import org.runcity.secure.SecureUserDetails;
import org.runcity.util.ResponseClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

@RestController
public class RestRuntimeController extends AbstractRestController {
	private static final Logger logger = Logger.getLogger(RestRuntimeController.class);
	
	@Autowired
	private VolunteerService volunteerService;
	
	@Autowired
	private TeamService teamService;
	
	@Autowired
	private ControlPointService controlPointService;
	
	@Autowired
	private RouteService routeService;
	
	public static class OnsiteRequestBody {
		@JsonView(Views.Public.class)
		private Long volunteer;
		
		@JsonView(Views.Public.class)
		private Boolean onsite;
		
		public OnsiteRequestBody() {
		}

		public Long getVolunteer() {
			return volunteer;
		}

		public void setVolunteer(Long volunteer) {
			this.volunteer = volunteer;
		}

		public Boolean getOnsite() {
			return onsite;
		}

		public void setOnsite(Boolean onsite) {
			this.onsite = onsite;
		}
	}
	
	private boolean checkVolunteer(Volunteer v) {
		return ObjectUtils.nullSafeEquals(v.getConsumer().getUsername(), SecureUserDetails.getCurrentUser().getUsername());
	}
	
	@JsonView(Views.Public.class)
	@Secured("ROLE_VOLUNTEER")
	@RequestMapping(value = "/api/v1/volunteer/onsite", method = RequestMethod.POST)
	public RestPostResponseBody onsite(@RequestBody OnsiteRequestBody request) {
		logger.info("POST /api/v1/volunteer/onsite");

		RestPostResponseBody result = new RestPostResponseBody(messageSource);
		
		if (request.getVolunteer() == null || request.getOnsite() == null) {
			result.setResponseClass(ResponseClass.ERROR);
			result.addCommonMsg("common.invalidRequest");
			return result;
		}
		
		Volunteer v = volunteerService.selectById(request.getVolunteer(), Volunteer.SelectMode.WITH_ACTIVE);
		
		if (v == null || !checkVolunteer(v)) {
			result.setResponseClass(ResponseClass.ERROR);
			result.addCommonMsg("volunteer.volunteerNotFound");
			return result;
		}
		
		if (ObjectUtils.nullSafeEquals(request.getOnsite(), v.getActive())) {
			// no action needed
			return result;
		}
		
		try {
			volunteerService.setCurrent(v, request.getOnsite());
		} catch (DBException e) {
			result.setResponseClass(ResponseClass.ERROR);
			result.addCommonMsg("common.db.fail");
			logger.error("DB exception", e);
			return result;			
		}
		
		return result;
	}
	
	@JsonView(Views.Public.class)
	@Secured("ROLE_VOLUNTEER")
	@RequestMapping(value = "/api/v1/volunteer/teamProcess", method = RequestMethod.POST)
	public RestPostResponseBody processTeam(@RequestBody TeamProcessByVolunteerForm form) {
		logger.info("POST /api/v1/volunteer/teamProcess");

		RestPostResponseBody result = new RestPostResponseBody(messageSource);
		Errors errors = validateForm(form, result);

		if (errors.hasErrors()) {
			return result;
		}
		
		if (!checkVolunteer(form.getVolunteer())) {
			result.setResponseClass(ResponseClass.ERROR);
			result.addCommonMsg("volunteer.volunteerNotFound");
			return result;
		}
		
		try {
			teamService.processTeam(form.getTeam(), form.getConfirmationToken(), form.getRouteItem(), form.getVolunteer(), result);
		} catch (DBException e) {
			logger.error(e);
			result.setResponseClass(ResponseClass.ERROR);
			result.addCommonMsg("common.popupProcessError");
		}
		
		if (result.getResponseClass() == ResponseClass.INFO) {
			result.addCommonMsg("common.completed");		
		}
		return result;
	}
	
	@JsonView(Views.Public.class)
	@Secured("ROLE_VOLUNTEER")
	@RequestMapping(value = "/api/v1/controlPoint/{cpId}/stat", method = RequestMethod.GET)
	public RestGetInfoResponseBody getStat(@PathVariable Long cpId) {
		RestGetInfoResponseBody result = new RestGetInfoResponseBody();

		Volunteer v = volunteerService.selectByControlPointAndUsername(cpId,
				SecureUserDetails.getCurrentUser().getUsername(), Volunteer.SelectMode.NONE);
		
		if (v == null || !checkVolunteer(v)) {
			result.setResponseClass(ResponseClass.ERROR);
			result.addCommonMsg("volunteer.volunteerNotFound");
			return result;
		}
		
		ControlPoint cp = controlPointService.selectById(v.getControlPoint().getId(), ControlPoint.SelectMode.WITH_CHILDREN_AND_ITEMS);
		cp = cp.getMain();
		
		Long total = 0L;
		for (RouteItem ri : cp.getRouteItems()) {
			Long stat = teamService.selectActiveNumberByRouteItem(ri);
			total += stat;
			result.addElement("routeCounter_" + ri.getId(), stat);
		}
		
		for (ControlPoint ch : cp.getChildren()) {
			for (RouteItem ri : ch.getRouteItems()) {
				Long stat = teamService.selectActiveNumberByRouteItem(ri);
				total += stat;
				result.addElement("routeCounter_" + ri.getId(), stat);
			}
		}
		
		result.addElement("routeCounter_total", total);
		
		return result;
	}
	
	@JsonView(VolunteerTeamTable.ForVolunteer.class)
	@Secured("ROLE_VOLUNTEER")
	@RequestMapping(value = "/api/v1/controlPoint/{cpId}/teamTable", method = RequestMethod.GET)
	public RestGetResponseBody getTeamsByCP(@PathVariable Long cpId) {
		Volunteer v = volunteerService.selectByControlPointAndUsername(cpId,
				SecureUserDetails.getCurrentUser().getUsername(), Volunteer.SelectMode.NONE);
		
		if (v == null || !checkVolunteer(v)) {
			RestGetResponseBody result = new RestGetResponseBody(messageSource);
			result.setResponseClass(ResponseClass.ERROR);
			result.addCommonMsg("volunteer.volunteerNotFound");
			return result;
		}
		
		VolunteerTeamTable result = VolunteerTeamTable.initRestResponse(messageSource);
		result.add(teamService.selectPendingTeamsByCP(cpId, Team.SelectMode.NONE));
		return result.validate();
	}

	@JsonView(VolunteerTeamTable.ForVolunteer.class)
	@Secured("ROLE_VOLUNTEER")
	@RequestMapping(value = "/api/v1/routeItem/{routeItemId}/teamTable", method = RequestMethod.GET)
	public RestGetResponseBody getTeamsByRouteItem(@PathVariable Long routeItemId) {
		RouteItem ri = routeService.selectItemById(routeItemId);
		
		if (ri == null) {
			RestGetResponseBody result = new RestGetResponseBody(messageSource);
			result.setResponseClass(ResponseClass.ERROR);
			result.addCommonMsg("volunteer.volunteerNotFound");
			return result;
		}
		
		Volunteer v = volunteerService.selectByControlPointAndUsername(ri.getControlPoint(),
				SecureUserDetails.getCurrentUser().getUsername(), Volunteer.SelectMode.NONE);
		
		if (v == null || !checkVolunteer(v)) {
			RestGetResponseBody result = new RestGetResponseBody(messageSource);
			result.setResponseClass(ResponseClass.ERROR);
			result.addCommonMsg("volunteer.volunteerNotFound");
			return result;
		}

		VolunteerTeamTable result = VolunteerTeamTable.initRestResponse(messageSource);
		result.add(teamService.selectPendingTeamsByRouteItem(ri, Team.SelectMode.NONE));
		return result.validate();
	}
}
