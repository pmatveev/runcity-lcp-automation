package org.runcity.mvc.config.util;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.runcity.secure.SecureUserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.support.RequestContextUtils;

public class UserLocaleChangeInterceptor extends HandlerInterceptorAdapter {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws ServletException {
		LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
		if (localeResolver == null) {
			throw new IllegalStateException("No LocaleResolver found: not in a DispatcherServlet request?");
		}

		String locale = SecureUserDetails.getCurrentUserLocale();
		if (locale != null) {
			localeResolver.setLocale(request, response, StringUtils.parseLocaleString(SecureUserDetails.getCurrentUserLocale()));
		} else {
			localeResolver.setLocale(request, response, null);
		}
		return true;
	}
}
