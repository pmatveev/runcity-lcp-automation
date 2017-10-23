<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@ include file="../template/head.jsp"%>
<body>
	<div class="row top-buffer">
		<div
			class="col-xs-offset-1 col-xs-10 col-sm-offset-2 col-sm-8 col-md-offset-3 col-md-6 col-lg-offset-4 col-lg-4">
			<form role="form" id="registerForm"
				onsubmit="return validateForm('registerForm', translations)">
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
						checkrequired="1" checkpassword="1" 
						onchange="(function() {checkPwd2('password', 'password2', translations); return checkPwdComplex('password', translations)})()" />
				</div>
				<div class="form-group">
					<label class="control-label" for="password2">
						<fmt:message key="register.password2" bundle="${msg}" />
					</label> 
					<input type="password" class="form-control" id="password2"
						placeholder="<fmt:message key="register.password2" bundle="${msg}" />"
						checkrequired="1" checkpwdequal="password" 
						onchange="return checkPwd2('password', 'password2', translations)"/>
				</div>
				<div class="form-group">
					<label class="control-label" for="credentials">
						<fmt:message key="register.credentials" bundle="${msg}" />
					</label> 
					<input type="text" class="form-control" id="credentials"
						placeholder="<fmt:message key="register.credentials" bundle="${msg}" />"
						checkrequired="1" />
				</div>
				<div class="form-group">
					<label class="control-label" for="email">
						<fmt:message key="register.email" bundle="${msg}" />
					</label> 
					<input type="text" class="form-control" id="email"
						placeholder="<fmt:message key="register.email" bundle="${msg}" />"
						checkrequired="1" />
				</div>
				<div class="form-group">
					<button type="submit" class="btn btn-primary">
						<fmt:message key="register.doregister" bundle="${msg}" />
					</button>
				</div>
			</form>
		</div>
	</div>
</body>
</html>