package org.runcity.mvc.validator;

import org.runcity.mvc.validator.sub.ConsumerFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ConsumerValidator implements Validator {
	@Autowired
	private ConsumerFormValidator consumerFormValidator;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return consumerFormValidator.supports(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		if (consumerFormValidator.supports(target.getClass())) {
			consumerFormValidator.validate(target, errors);
		}
	}

}
