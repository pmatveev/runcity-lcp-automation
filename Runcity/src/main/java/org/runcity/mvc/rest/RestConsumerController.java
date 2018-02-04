package org.runcity.mvc.rest;

import java.util.List;

import org.apache.log4j.Logger;
import org.runcity.db.entity.Consumer;
import org.runcity.db.service.ConsumerService;
import org.runcity.exception.DBException;
import org.runcity.exception.EMailException;
import org.runcity.mvc.rest.util.RestGetResponseBody;
import org.runcity.mvc.rest.util.RestPostResponseBody;
import org.runcity.mvc.rest.util.RestResponseClass;
import org.runcity.mvc.rest.util.Views;
import org.runcity.mvc.web.formdata.*;
import org.runcity.mvc.web.tabledata.ConsumerTable;
import org.runcity.secure.SecureUserDetails;
import org.runcity.util.CommonProperties;
import org.runcity.util.DynamicLocaleList;
import org.runcity.util.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

@RestController
public class RestConsumerController extends AbstractRestController {
	private static final Logger logger = Logger.getLogger(RestConsumerController.class);

	@Autowired
	private ConsumerService consumerService;
	
	@Autowired
	private DynamicLocaleList localeList;
	
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
			result.setResponseClass(RestResponseClass.ERROR);
			result.addCommonError("common.db.fail");
		}

		result.setResponseClass(RestResponseClass.INFO);
		return result;
	}
	
	@JsonView(Views.Public.class)
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
			result.setResponseClass(RestResponseClass.ERROR);
			result.addCommonError("common.db.fail");
		}

		result.setResponseClass(RestResponseClass.INFO);
		return result;
	}	

	@JsonView(Views.Public.class)
	@RequestMapping(value = "/api/v1/consumerSelfEdit", method = RequestMethod.GET)
	public RestGetResponseBody initConsumerSelfEditForm() {
		logger.info("GET /api/v1/consumerSelfEdit");

		Consumer c = consumerService.getCurrent();
		if (c == null) {
			RestGetResponseBody result = new RestGetResponseBody(messageSource);
			result.setResponseClass(RestResponseClass.ERROR);
			result.addCommonError("common.popupFetchError");
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
			result.setResponseClass(RestResponseClass.ERROR);
			result.addCommonError("common.popupProcessError");
		}
		return result;
	}
	
	@JsonView(Views.Public.class)
	@RequestMapping(value = "/api/v1/consumerCreate", method = RequestMethod.POST)
	@Secured("ROLE_ADMIN")
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
			result.setResponseClass(RestResponseClass.ERROR);
			result.addCommonError("common.db.fail");
			logger.error("DB exception", e);
			return result;
		}
		if (c == null) {
			result.setResponseClass(RestResponseClass.ERROR);
			result.addCommonError("common.popupProcessError");
		}
		return result;
	}
	
	@JsonView(Views.Public.class)
	@RequestMapping(value = "/api/v1/consumerEdit/{id}", method = RequestMethod.GET)
	public RestGetResponseBody initConsumerEditForm(@PathVariable Long id) {
		logger.info("GET /api/v1/consumerSelfEdit/" + id);

		Consumer c = consumerService.selectById(id, true);
		if (c == null) {
			RestGetResponseBody result = new RestGetResponseBody(messageSource);
			result.setResponseClass(RestResponseClass.ERROR);
			result.addCommonError("common.popupFetchError");
			return result;
		}

		return new ConsumerEditForm(c, localeList);
	}

	@JsonView(Views.Public.class)
	@RequestMapping(value = "/api/v1/consumerEdit", method = RequestMethod.POST)
	@Secured("ROLE_ADMIN")
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
			result.setResponseClass(RestResponseClass.ERROR);
			result.addCommonError("common.db.fail");
			logger.error("DB exception", e);
			return result;
		}
		if (c == null) {
			result.setResponseClass(RestResponseClass.ERROR);
			result.addCommonError("common.popupProcessError");
		}
		return result;
	}
	
	@JsonView(Views.Public.class)
	@RequestMapping(value = "/api/v1/consumerDelete", method = RequestMethod.DELETE)
	@Secured("ROLE_ADMIN")
	public RestPostResponseBody consumerDelete(@RequestBody List<Long> id) {
		logger.info("DELETE /api/v1/consumerDelete");
		RestPostResponseBody result = new RestPostResponseBody(messageSource);
		
		Long myId = SecureUserDetails.getCurrent().getId();
		for (Long i : id) {
			if (ObjectUtils.nullSafeEquals(myId, i)) {
				result.setResponseClass(RestResponseClass.ERROR);
				result.addCommonError("user.selfDelete");
				return result;
			}
		}
		
		try {
			consumerService.delete(id);
		} catch (Exception e) {
			result.setResponseClass(RestResponseClass.ERROR);
			result.addCommonError("commom.db.deleteConstraint");
		}
		return result;		
	}
	
	@JsonView(Views.Public.class)
	@RequestMapping(value = "/api/v1/consumerTable", method = RequestMethod.GET)
	public ConsumerTable getConsumerTable() {
		logger.info("GET /api/v1/consumerTable");
		ConsumerTable table = new ConsumerTable(null, messageSource, localeList);
		table.fetchAll(consumerService);
		return table;
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
			consumerService.recoverPassword(form.getConsumer(), commonProperties, messageSource,  LocaleContextHolder.getLocale());
		} catch (DBException | EMailException e) {
			result.setResponseClass(RestResponseClass.ERROR);
			result.addCommonError("common.db.fail");
		}
		
		result.setResponseClass(RestResponseClass.INFO);
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
			result.setResponseClass(RestResponseClass.ERROR);
			result.addCommonError("common.db.fail");
		}

		if (c == null) {
			result.setResponseClass(RestResponseClass.ERROR);
			result.addCommonError("common.popupProcessError");
		}
		
		result.setResponseClass(RestResponseClass.INFO);
		return result;
	}
}
