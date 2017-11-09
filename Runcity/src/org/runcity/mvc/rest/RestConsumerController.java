package org.runcity.mvc.rest;

import org.apache.log4j.Logger;
import org.runcity.db.entity.Consumer;
import org.runcity.db.service.ConsumerService;
import org.runcity.exception.DBException;
import org.runcity.mvc.rest.util.RestGetResponseBody;
import org.runcity.mvc.rest.util.RestPostResponseBody;
import org.runcity.mvc.rest.util.RestResponseClass;
import org.runcity.mvc.rest.util.Views;
import org.runcity.mvc.validator.FormValidator;
import org.runcity.mvc.web.formdata.AbstractForm;
import org.runcity.mvc.web.formdata.ChangePasswordByPasswordForm;
import org.runcity.mvc.web.formdata.ConsumerSelfEditForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

@RestController
public class RestConsumerController {
	private static final Logger logger = Logger.getLogger(RestConsumerController.class);

	@Autowired
	private FormValidator formValidator;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private ConsumerService consumerService;

	private Errors validateForm(AbstractForm form, RestPostResponseBody result) {
		Errors errors = new BindException(form, form.getFormName());
		if (formValidator.supports(form.getClass())) {
			formValidator.validate(form, errors);
		}
		if (errors.hasErrors()) {
			result.setResponseClass(RestResponseClass.ERROR);
			result.parseErrors(errors);
		}
		return errors;
	}

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
			result.addCommonError("mvc.db.fail");
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

		return new ConsumerSelfEditForm(c.getUsername(), c.getCredentials(), c.getEmail());
	}

	@JsonView(Views.Public.class)
	@RequestMapping(value = "/api/v1/consumerSelfEdit", method = RequestMethod.POST)
	public RestPostResponseBody ConsumerSelfEdit(@RequestBody ConsumerSelfEditForm form) {
		logger.info("POST /api/v1/consumerSelfEdit");

		RestPostResponseBody result = new RestPostResponseBody(messageSource);
		Errors errors = validateForm(form, result);

		if (errors.hasErrors()) {
			return result;
		}
		
		Consumer c = consumerService.updateCurrentData(form.getUsername(), form.getCredentials(), form.getEmail());
		if (c == null) {
			result.setResponseClass(RestResponseClass.ERROR);
			result.addCommonError("common.popupProcessError");
		}
		return result;
	}
}
