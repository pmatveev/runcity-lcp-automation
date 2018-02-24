package org.runcity.mvc.web.util;

import org.runcity.db.entity.Consumer;
import org.runcity.db.entity.ConsumerRole;
import org.runcity.db.service.ConsumerService;
import org.runcity.mvc.web.formdata.AbstractForm;
import org.runcity.secure.SecureUserRole;
import org.runcity.util.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.Errors;

public class FormDddwConsumerColumn extends FormDddwColumn<Long> {
	private enum FetchType {
		ALL_BY_ALGORITHM;
	}

	private Consumer consumer;
	private FetchType fetchType;

	private FormDddwConsumerColumn(AbstractForm form, ColumnDefinition definition, String ajaxSource,
			FormColumn<?>[] ajaxParms, FetchType fetchType, Object... validation) {
		super(form, definition, "/api/v1/dddw/consumerId?id={0}", null, ajaxSource, ajaxParms, false, validation);
		this.fetchType = fetchType;
	}

	public static FormDddwConsumerColumn getAll(AbstractForm form, ColumnDefinition definition, Boolean active, SecureUserRole ... roles) {
		String ajax = "/api/v1/dddw/consumer?active=" + (active == null ? "" : active.toString()) + "&roles=" + StringUtils.concatNvl(",", (Object[]) roles);
		return new FormDddwConsumerColumn(form, definition, ajax, null, FetchType.ALL_BY_ALGORITHM, active, roles);
	}
	
	public Consumer getConsumer() {
		return consumer;
	}

	@Override
	public void validate(ApplicationContext context, Errors errors) {
		super.validate(context, errors);
		
		if (value == null) {
			return;
		}
		
		ConsumerService consumerService = context.getBean(ConsumerService.class);
		consumer = consumerService.selectById(value, true);
		
		if (consumer == null) {
			errors.rejectValue(getName(), "common.notFoundId", new Object[] { value }, null);
			return;
		}
		
		switch (fetchType) {
		case ALL_BY_ALGORITHM:
			/*
			 * validation 
			 * 	0: active 
			 * 	1: roles
			 */
			Boolean active = (Boolean) validation[0];
			if (active != null && !ObjectUtils.nullSafeEquals(active, consumer.isActive())) {
				errors.rejectValue(getName(), "user.validation.invalidActive", new Object[] { active.toString() }, null);
			}
			
			SecureUserRole[] roles = (SecureUserRole[]) validation[1];
			if (roles != null) {
				boolean ok = false;
				for (SecureUserRole r : roles) {
					for (ConsumerRole cr : consumer.getRoles()) {
						if (ObjectUtils.nullSafeEquals(r.toString(), cr.getCode())) {
							ok = true;
							break;
						}
					}
					if (ok) {
						break;
					}
				}
				if (!ok) {
					errors.rejectValue(getName(), "user.validation.roleMissing", new Object[] { StringUtils.toString(roles, ", ", "") }, null);					
				}
			}
		}
	}
}
