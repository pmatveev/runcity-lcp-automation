package org.runcity.mvc.config.util;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.runcity.util.StringUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

public class CommonAccessDeniedHandler implements AccessDeniedHandler {
	private static final Logger logger = Logger.getLogger(CommonAccessDeniedHandler.class);
	private String page403;
	
	public CommonAccessDeniedHandler(String page403) {
		this.page403 = page403;
	}
	
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e)
			throws IOException, ServletException {
		logger.info("AccessDeniedHandler");

		if (StringUtils.isEqual(request.getContentType(), "application/json")) {
			// REST
	        response.sendError(403);
		} else {
	        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
	        RequestDispatcher dispatcher = request.getRequestDispatcher(page403);
	        dispatcher.forward(request, response);
		}
	}

}
