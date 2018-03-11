package org.runcity.mvc.rest;

import org.apache.log4j.Logger;
import org.runcity.db.entity.Volunteer;
import org.runcity.db.service.VolunteerService;
import org.runcity.exception.DBException;
import org.runcity.mvc.rest.util.RestPostResponseBody;
import org.runcity.mvc.rest.util.RestResponseClass;
import org.runcity.mvc.rest.util.Views;
import org.runcity.secure.SecureUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

@RestController
public class RuntimeController extends AbstractRestController {
	private static final Logger logger = Logger.getLogger(RuntimeController.class);
	
	@Autowired
	private VolunteerService volunteerService;
	
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
	
	@JsonView(Views.Public.class)
	@Secured("ROLE_VOLUNTEER")
	@RequestMapping(value = "/api/v1/volunteerOnsite", method = RequestMethod.POST)
	public RestPostResponseBody onsite(@RequestBody OnsiteRequestBody request) {
		logger.info("POST /api/v1/volunteerOnsite");

		RestPostResponseBody result = new RestPostResponseBody(messageSource);
		
		if (request.getVolunteer() == null || request.getOnsite() == null) {
			result.setResponseClass(RestResponseClass.ERROR);
			result.addCommonMsg("common.invalidRequest");
			return result;
		}
		
		Volunteer v = volunteerService.selectById(request.getVolunteer(), Volunteer.SelectMode.WITH_ACTIVE);
		
		if (v == null || !ObjectUtils.nullSafeEquals(v.getConsumer().getUsername(), SecureUserDetails.getCurrentUser().getUsername())) {
			result.setResponseClass(RestResponseClass.ERROR);
			result.addCommonMsg("jsp.controlPoint.validation.volunteerNotFound");
			return result;
		}
		
		if (ObjectUtils.nullSafeEquals(request.getOnsite(), v.isActive())) {
			// no action needed
			return result;
		}
		
		try {
			volunteerService.setCurrent(v, request.getOnsite());
		} catch (DBException e) {
			result.setResponseClass(RestResponseClass.ERROR);
			result.addCommonMsg("common.db.fail");
			logger.error("DB exception", e);
			return result;			
		}
		
		return result;
	}
}
