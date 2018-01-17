<%@ include file="../template/head.jsp"%>
<spring:url value="/j_spring_security_check" var="doLogin" />
<fmt:message key="user.username" bundle="${msg}" var="msgUsername"/>
<fmt:message key="user.password" bundle="${msg}" var="msgPassword"/>
<fmt:message key="login.keepLogin" bundle="${msg}" var="msgKeepLogin"/>
<div class="container form-container login-container static-margin-top">
	<h1>
		<fmt:message key="login.header" bundle="${msg}" />
	</h1>
	<form method="post" action="${doLogin}" id="loginForm" onsubmit="return validateForm($('#loginForm'))">
		<input type="hidden"  name="${_csrf.parameterName}"   value="${_csrf.token}"/>
		<div class="errorHolder">
			<c:if test="${!empty error}">
				<fmt:message key="${error}" bundle="${msg}" var="errorMsg"/>
				<div class="alert alert-danger">
					${errorMsg}
				</div>
			</c:if>
		</div>
		<div class="form-group">
			<label class="control-label" for="username">
				<c:out value="${msgUsername}"/>
			</label> 
			<input type="text" id="j_username" name="j_username"  class="form-control" 
				placeholder="${msgUsername}" jschecks="required;" onchange="checkInput($('#j_username'))"/>
		</div>			

		<div class="form-group">
			<label class="control-label" for="password">
				<c:out value="${msgPassword}"/>
			</label> 
			<input type="password" id="j_password" name="j_password" class="form-control" 
				placeholder="${msgPassword}" jschecks="required;" onchange="checkInput($('#j_password'))"/>
		</div>			

		<div class="form-group">
			<div class="checkbox">
				<label>
					<input type="checkbox" id="remember-me" name="remember-me" /><c:out value="${msgKeepLogin}"/>
				</label>
			</div>					
		</div>
		<button type="submit" class="btn btn-primary btn-block">
			<fmt:message key="login.dologin" bundle="${msg}" />
		</button>

		<a class="btn btn-link btn-block" href="#"><fmt:message key="login.reissuePassword" bundle="${msg}" /></a>
	</form>			
</div>
<%@ include file="../template/foot.jsp"%>