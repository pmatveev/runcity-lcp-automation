<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@ include file="../template/head.jsp"%>
<body>
	<div class="row top-buffer">
		<div
			class="col-xs-offset-1 col-xs-10 col-sm-offset-2 col-sm-8 col-md-offset-3 col-md-6 col-lg-offset-4 col-lg-4">
			<form role="form" id="loginForm"
				onsubmit="return validateForm('loginForm', translations)">
				<div class="form-group">
					<label class="control-label" for="username">
						<fmt:message key="login.username" bundle="${msg}" />
					</label> 
					<input type="text" class="form-control" id="username"
						placeholder="<fmt:message key="login.username" bundle="${msg}" />"
						checkrequired="1" />
				</div>
				<div class="form-group">
					<label class="control-label" for="password">
						<fmt:message key="login.password" bundle="${msg}" />
					</label> 
					<input type="password" class="form-control" id="password"
						placeholder="<fmt:message key="login.password" bundle="${msg}" />"
						checkrequired="1" />
				</div>
				<div class="checkbox">
					<label> <input type="checkbox" id="keepLogin" /> <fmt:message
							key="login.keepLogin" bundle="${msg}" />
					</label>
					<button type="submit" class="btn btn-primary right-aligned">
						<fmt:message key="login.dologin" bundle="${msg}" />
					</button>
				</div>
			</form>
		</div>
	</div>
	<div class="row">
		<div class="col-xs-offset-2 col-xs-4 col-sm-offset-3 col-sm-3 col-md-offset-4 col-md-2 col-lg-offset-5 col-lg-1">
			<a class="btn-link" href="/Runcity/common/register.jsp"><fmt:message key="login.newUser"
					bundle="${msg}" /></a>
		</div>
		<div class="col-xs-4 col-sm-3 col-md-2 col-lg-1">
			<a class="btn-link right-aligned" href="#"><fmt:message
					key="login.reissuePassword" bundle="${msg}" /></a>
		</div>
	</div>
</body>
</html>