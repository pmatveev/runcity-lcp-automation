package org.runcity.mvc.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.MDC;

@WebFilter(urlPatterns = { "/*" }, description = "Session Checker Filter")
public class SessionFilter implements Filter {

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		try {
			MDC.put("sessionId", ((HttpServletRequest) req).getSession().getId());
			chain.doFilter(req, res);
		} finally {
			MDC.remove("sessionId");
		}
	}

}
