package org.runcity.mvc.validator.sub;

import org.runcity.db.service.ConsumerService;
import org.runcity.mvc.validator.util.EmailValidator;
import org.runcity.mvc.validator.util.PasswordValidator;
import org.runcity.mvc.web.formdata.ConsumerForm;
import org.runcity.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class ConsumerFormValidator implements Validator {
	private EmailValidator emailValidator = new EmailValidator();
	private PasswordValidator passwordValidator = new PasswordValidator();
	
	@Autowired
	private ConsumerService consumerService;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return ConsumerForm.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ConsumerForm form = (ConsumerForm) target;

		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "mvc.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "mvc.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password2", "mvc.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "credentials", "mvc.required");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "mvc.required");
		
		if (!emailValidator.valid(form.getEmail().getValue())) {
			errors.rejectValue("email", "js.invalidEmail");
		}
		
		if (!passwordValidator.valid(form.getPassword().getValue())) {
			errors.rejectValue("password", "js.passwordStrength");
		}
		
		if (!StringUtils.isEqual(form.getPassword().getValue(), form.getPassword2().getValue())) {
			errors.rejectValue("password2", "js.passwordMatch");
		}
		
		if (consumerService.selectByUsername(form.getUsername().getValue()) != null) {
			errors.rejectValue("username", "mvc.userExists"); 
		}
		
		if (consumerService.selectByEmail(form.getEmail().getValue()) != null) {
			errors.rejectValue("email", "mvc.emailExists");
		}
	}	
}
