package org.runcity.mvc.rest;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.runcity.db.entity.Consumer;
import org.runcity.db.entity.ConsumerRole;
import org.runcity.db.entity.Volunteer;
import org.runcity.db.entity.enumeration.SecureUserRole;
import org.runcity.db.service.ConsumerService;
import org.runcity.db.service.VolunteerService;
import org.runcity.exception.DBException;
import org.runcity.exception.EMailException;
import org.runcity.mvc.rest.util.RestGetDddwResponseBody;
import org.runcity.mvc.rest.util.RestGetResponseBody;
import org.runcity.mvc.rest.util.RestPostResponseBody;
import org.runcity.mvc.rest.util.Views;
import org.runcity.mvc.web.formdata.*;
import org.runcity.mvc.web.tabledata.ConsumerTable;
import org.runcity.mvc.web.tabledata.VolunteerTable;
import org.runcity.secure.SecureUserDetails;
import org.runcity.util.CommonProperties;
import org.runcity.util.ResponseClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

@RestController
public class RestConsumerController extends AbstractRestController {
	private static final Logger logger = Logger.getLogger(RestConsumerController.class);

	@Autowired
	private ConsumerService consumerService;

	@Autowired
	private VolunteerService volunteerService;
	
	@Autowired
	private CommonProperties commonProperties;

	@JsonView(Views.Public.class)
	@RequestMapping(value = "/api/v1/changePasswordByPassword", method = RequestMethod.POST)
	public RestPostResponseBody changePasswordByPassword(@RequestBody ChangePasswordByPasswordForm form) {
		logger.info("POST /api/v1/changePasswordByPassword");

		RestPostResponseBody result = new RestPostResponseBody(messageSource);
		Errors errors = validateForm(form, result);

		if (errors.hasErrors()) {
			return result;
		}

		try {
			consumerService.updateCurrentPassword(form.getPassword());
		} catch (DBException e) {
			result.setResponseClass(ResponseClass.ERROR);
			result.addCommonMsg("common.db.fail");
		}

		result.setResponseClass(ResponseClass.INFO);
		result.addCommonMsg("changePassword.info");
		return result;
	}
	
	@JsonView(Views.Public.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/api/v1/changePasswordById", method = RequestMethod.POST)
	public RestPostResponseBody changePasswordById(@RequestBody ChangePasswordByIdForm form) {
		logger.info("POST /api/v1/changePasswordById");

		RestPostResponseBody result = new RestPostResponseBody(messageSource);
		Errors errors = validateForm(form, result);

		if (errors.hasErrors()) {
			return result;
		}

		try {
			consumerService.updatePassword(form.getId(), form.getPassword());
		} catch (DBException e) {
			result.setResponseClass(ResponseClass.ERROR);
			result.addCommonMsg("common.db.fail");
		}

		result.setResponseClass(ResponseClass.INFO);
		return result;
	}	

	@JsonView(Views.Public.class)
	@RequestMapping(value = "/api/v1/consumerSelfEdit", method = RequestMethod.GET)
	public RestGetResponseBody initConsumerSelfEditForm() {
		logger.info("GET /api/v1/consumerSelfEdit");

		Consumer c = consumerService.getCurrent();
		if (c == null) {
			RestGetResponseBody result = new RestGetResponseBody(messageSource);
			result.setResponseClass(ResponseClass.ERROR);
			result.addCommonMsg("common.popupFetchError");
			return result;
		}

		return new ConsumerSelfEditForm(c, localeList);
	}

	@JsonView(Views.Public.class)
	@RequestMapping(value = "/api/v1/consumerSelfEdit", method = RequestMethod.POST)
	public RestPostResponseBody consumerSelfEdit(@RequestBody ConsumerSelfEditForm form) {
		logger.info("POST /api/v1/consumerSelfEdit");

		RestPostResponseBody result = new RestPostResponseBody(messageSource);
		Errors errors = validateForm(form, result);

		if (errors.hasErrors()) {
			return result;
		}
		
		Consumer c = consumerService.updateCurrentData(form.getUsername(), form.getCredentials(), form.getEmail(), form.getLocale());
		if (c == null) {
			result.setResponseClass(ResponseClass.ERROR);
			result.addCommonMsg("common.popupProcessError");
		}
		
		result.addCommonMsg("common.completed");
		return result;
	}
	
	@JsonView(Views.Public.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/api/v1/consumerCreate", method = RequestMethod.POST)
	public RestPostResponseBody consumerCreate(@RequestBody ConsumerCreateForm form) {
		logger.info("POST /api/v1/consumerCreate");

		RestPostResponseBody result = new RestPostResponseBody(messageSource);
		Errors errors = validateForm(form, result);

		if (errors.hasErrors()) {
			return result;
		}
		
		Consumer c = null;
		try {
			c = consumerService.add(form.getConsumer());
		} catch (DBException e) {
			result.setResponseClass(ResponseClass.ERROR);
			result.addCommonMsg("common.db.fail");
			logger.error("DB exception", e);
			return result;
		}
		if (c == null) {
			result.setResponseClass(ResponseClass.ERROR);
			result.addCommonMsg("common.popupProcessError");
		}
		return result;
	}
	
	@JsonView(Views.Public.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/api/v1/consumerEdit/{id}", method = RequestMethod.GET)
	public RestGetResponseBody initConsumerEditForm(@PathVariable Long id) {
		logger.info("GET /api/v1/consumerSelfEdit/" + id);

		Consumer c = consumerService.selectById(id, Consumer.SelectMode.WITH_ROLES);
		if (c == null) {
			RestGetResponseBody result = new RestGetResponseBody(messageSource);
			result.setResponseClass(ResponseClass.ERROR);
			result.addCommonMsg("common.popupFetchError");
			return result;
		}

		return new ConsumerEditForm(c, localeList);
	}

	@JsonView(Views.Public.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/api/v1/consumerEdit", method = RequestMethod.POST)
	public RestPostResponseBody consumerEdit(@RequestBody ConsumerEditForm form) {
		logger.info("POST /api/v1/consumerEdit");

		RestPostResponseBody result = new RestPostResponseBody(messageSource);
		Errors errors = validateForm(form, result);

		if (errors.hasErrors()) {
			return result;
		}
		
		Consumer c = null;
		try {
			c = consumerService.update(form.getConsumer());
		} catch (DBException e) {
			result.setResponseClass(ResponseClass.ERROR);
			result.addCommonMsg("common.db.fail");
			logger.error("DB exception", e);
			return result;
		}
		if (c == null) {
			result.setResponseClass(ResponseClass.ERROR);
			result.addCommonMsg("common.popupProcessError");
		}
		return result;
	}
	
	@JsonView(Views.Public.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/api/v1/consumerDelete", method = RequestMethod.DELETE)
	public RestPostResponseBody consumerDelete(@RequestBody List<Long> id) {
		logger.info("DELETE /api/v1/consumerDelete");
		RestPostResponseBody result = new RestPostResponseBody(messageSource);
		
		Long myId = SecureUserDetails.getCurrentUser().getId();
		for (Long i : id) {
			if (ObjectUtils.nullSafeEquals(myId, i)) {
				result.setResponseClass(ResponseClass.ERROR);
				result.addCommonMsg("user.selfDelete");
				return result;
			}
		}
		
		try {
			consumerService.delete(id);
		} catch (Exception e) {
			result.setResponseClass(ResponseClass.ERROR);
			result.addCommonMsg("commom.db.deleteConstraint");
		}
		return result;		
	}
	
	@JsonView(Views.Public.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/api/v1/consumerTable", method = RequestMethod.GET)
	public ConsumerTable getConsumerTable() {
		logger.info("GET /api/v1/consumerTable");
		ConsumerTable table = new ConsumerTable(messageSource, localeList);
		table.fetchAll(consumerService);
		return table.validate();
	}
	
	@JsonView(Views.Public.class)
	@RequestMapping(value = "/common/api/v1/passwordRecovery", method = RequestMethod.POST)
	public RestPostResponseBody initPasswordRecovery(@RequestBody PasswordRecoveryForm form) {
		logger.info("POST /common/api/v1/passwordRecovery");

		RestPostResponseBody result = new RestPostResponseBody(messageSource);
		Errors errors = validateForm(form, result);

		if (errors.hasErrors()) {
			return result;
		}

		try {
			consumerService.recoverPassword(form.getConsumer(), commonProperties, messageSource);
		} catch (DBException | EMailException e) {
			result.setResponseClass(ResponseClass.ERROR);
			result.addCommonMsg("common.db.fail");
			return result;
		}
		
		if (result.getResponseClass() == ResponseClass.INFO) {
			result.addCommonMsg("passwordRecovery.sent");
		}
		
		return result;
	}
	
	@JsonView(Views.Public.class)
	@RequestMapping(value = "/common/api/v1/register", method = RequestMethod.POST)
	public RestPostResponseBody register(@RequestBody ConsumerRegisterForm form) {
		logger.info("POST /common/api/v1/register");

		RestPostResponseBody result = new RestPostResponseBody(messageSource);
		Errors errors = validateForm(form, result);

		if (errors.hasErrors()) {
			return result;
		}

		Consumer c = null;
		try {
			c = consumerService.register(form.getUsername(), form.getPassword(), form.getCredentials(), form.getEmail(), form.getLocale());
		} catch (DBException e) {
			result.setResponseClass(ResponseClass.ERROR);
			result.addCommonMsg("common.db.fail");
		}

		if (c == null) {
			result.setResponseClass(ResponseClass.ERROR);
			result.addCommonMsg("common.popupProcessError");
		}
		
		return result;
	}
	
	@JsonView(VolunteerTable.ByConsumerControlPoint.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/api/v1/volunteerTableByConsumer", method = RequestMethod.GET)
	public VolunteerTable getVolunteersTable(@RequestParam(required = true) Long consumerId) {
		logger.info("GET /api/v1/volunteerTableByConsumer");
		logger.debug("\tconsumerId=" + consumerId);
		
		Consumer c = consumerService.selectById(consumerId, Consumer.SelectMode.NONE);
		
		VolunteerTable table = VolunteerTable.initVolunteersByConsumer(messageSource, localeList, c);
		table.add(volunteerService.selectVolunteersByConsumer(c, Volunteer.SelectMode.NONE));
		return table.validate();
	}	
	
	@JsonView(VolunteerTable.ByConsumerGame.class)
	@Secured("ROLE_ADMIN")
	@RequestMapping(value = "/api/v1/coordinatorTableByConsumer", method = RequestMethod.GET)
	public VolunteerTable getCoordinatorsTable(@RequestParam(required = true) Long consumerId) {
		logger.info("GET /api/v1/coordinatorTableByConsumer");
		logger.debug("\tconsumerId=" + consumerId);
		
		Consumer c = consumerService.selectById(consumerId, Consumer.SelectMode.NONE);
		
		VolunteerTable table = VolunteerTable.initCoordinatorsByConsumer(messageSource, localeList, c);
		table.add(volunteerService.selectCoordinatorsByConsumer(c, Volunteer.SelectMode.NONE));
		return table.validate();
	}	
	
	@JsonView(Views.Public.class)
	@Secured({ "ROLE_ADMIN", "ROLE_VOLUNTEER" })
	@RequestMapping(value = "/api/v1/dddw/consumerId", method = RequestMethod.GET)
	public RestGetResponseBody consumerDddwInit(@RequestParam(required = true) Long id) {
		logger.info("GET /api/v1/dddw/consumerId");
		Consumer c = consumerService.selectById(id, Consumer.SelectMode.NONE);
		RestGetDddwResponseBody<Long> result = new RestGetDddwResponseBody<Long>(messageSource);
		result.addOption(id, c.getCredentials());
		return result;
	}
	
	@JsonView(Views.Public.class)
	@Secured({ "ROLE_ADMIN", "ROLE_VOLUNTEER" })
	@RequestMapping(value = "/api/v1/dddw/consumer", method = RequestMethod.GET)
	public RestGetResponseBody consumerDddw(@RequestParam(required = false) Boolean active, @RequestParam(required = false) List<String> roles) {
		logger.info("GET /api/v1/dddw/consumer");
		boolean checkActive = active != null;
		boolean checkRoles = roles != null && roles.size() > 0;
		
		List<Consumer> consumers = consumerService.selectAll(checkRoles ? Consumer.SelectMode.WITH_ROLES : Consumer.SelectMode.NONE);
		RestGetDddwResponseBody<Long> result = new RestGetDddwResponseBody<Long>(messageSource);
		for (Consumer c : consumers) {
			if (checkActive && !ObjectUtils.nullSafeEquals(c.isActive(), active)) {
				continue;
			}
			if (checkRoles) {
				List<SecureUserRole> parsedRoles = new ArrayList<SecureUserRole>();
				
				for (String s : roles) {
					SecureUserRole r = SecureUserRole.getByStoredValue(s);
					if (r != null) {
						parsedRoles.add(r);
					}
				}
				
				boolean ok = false;
				for (ConsumerRole r : c.getRoles()) {
					if (parsedRoles.contains(r.getRole())) {
						ok = true;
						break;
					}
				}
				if (!ok) {
					continue;
				}
			}
			result.addOption(c.getId(), c.getCredentials());
		}
		return result;
	}
}
