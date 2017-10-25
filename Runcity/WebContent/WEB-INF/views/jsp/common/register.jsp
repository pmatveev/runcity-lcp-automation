<%@ include file="../template/head.jsp"%>
<spring:url value="/register" var="doRegister" />
<fmt:message key="user.credentials" bundle="${msg}" var="msgCredentials"/>
<fmt:message key="user.username" bundle="${msg}" var="msgUsername"/>
<fmt:message key="login.password" bundle="${msg}" var="msgPassword"/>
<fmt:message key="register.password2" bundle="${msg}" var="msgPassword2"/>
<fmt:message key="user.email" bundle="${msg}" var="msgEmail"/>

<div class="row top-buffer">
	<div class="col-xs-offset-1 col-xs-10 col-sm-offset-2 col-sm-8 col-md-offset-3 col-md-6 col-lg-offset-4 col-lg-4" >
		<h1>
			<fmt:message key="register.header" bundle="${msg}" />
		</h1>
		<form:form method="post" modelAttribute="registerForm" action="${doRegister}" id="registerForm" onsubmit="return validateForm('registerForm', translations)">
			<input type="hidden"  name="${_csrf.parameterName}"   value="${_csrf.token}"/>
			<form:errors cssClass="alert alert-danger" element="div"/>
			<spring:bind path="credentials">
				<div class="form-group ${status.error ? 'has-error' : ''}">
					<label class="control-label" for="credentials">
						<c:out value="${msgCredentials}"/>
					</label> 
					<form:input path="credentials" id="credentials" type="text" class="form-control" placeholder="${msgCredentials}" checkrequired="1" />
					<form:errors path="credentials" class="help-block"/>
				</div>			
			</spring:bind>		

			<spring:bind path="username">
				<div class="form-group ${status.error ? 'has-error' : ''}">
					<label class="control-label" for="username">
						<c:out value="${msgUsername}"/>
					</label> 
					<form:input path="username" id="username" type="text" class="form-control" placeholder="${msgUsername}" checkrequired="1" />
					<form:errors path="username" class="help-block"/>
				</div>			
			</spring:bind>

			<spring:bind path="password">
				<div class="form-group ${status.error ? 'has-error' : ''}">
					<label class="control-label" for="password">
						<c:out value="${msgPassword}"/>
					</label> 
					<form:password path="password" id="password" class="form-control" placeholder="${msgPassword}"
						checkrequired="1" checkpassword="1" 
						onchange="(function() {checkPwd2('password', 'password2', translations); return checkPwdComplex('password', translations)})()"/>
					<form:errors path="password" class="help-block"/>
				</div>			
			</spring:bind>

			<spring:bind path="password2">
				<div class="form-group ${status.error ? 'has-error' : ''}">
					<label class="control-label" for="password2">
						<c:out value="${msgPassword2}"/>
					</label> 
					<form:password path="password2" id="password2" class="form-control" placeholder="${msgPassword2}"
						checkrequired="1" checkpwdequal="password" 
						onchange="return checkPwd2('password', 'password2', translations)"/>
					<form:errors path="password2" class="help-block"/>
				</div>			
			</spring:bind>	

			<spring:bind path="email">
				<div class="form-group ${status.error ? 'has-error' : ''}">
					<label class="control-label" for="email">
						<c:out value="${msgEmail}"/>
					</label> 
					<form:input path="email" id="email" type="text" class="form-control" placeholder="${msgEmail}" 
						checkrequired="1" checkemail="1"
						onchange="return checkEmail('email', translations)" />
					<form:errors path="email" class="help-block"/>
				</div>			
			</spring:bind>
			
			<div class="form-group">
				<button type="submit" class="btn btn-primary">
					<fmt:message key="register.doregister" bundle="${msg}" />
				</button>
			</div>
		</form:form>
	</div>
</div>
<%@ include file="../template/foot.jsp"%>