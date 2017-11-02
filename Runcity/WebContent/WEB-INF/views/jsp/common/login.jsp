<%@ include file="../template/head.jsp"%>
<spring:url value="/j_spring_security_check" var="doLogin" />
<fmt:message key="user.username" bundle="${msg}" var="msgUsername"/>
<fmt:message key="login.password" bundle="${msg}" var="msgPassword"/>
<fmt:message key="login.keepLogin" bundle="${msg}" var="msgKeepLogin"/>
<div class="row top-buffer">
	<div class="col-xs-offset-1 col-xs-10 col-sm-offset-2 col-sm-8 col-md-offset-3 col-md-6 col-lg-offset-4 col-lg-4">
		<h1>
			<fmt:message key="login.header" bundle="${msg}" />
		</h1>
		<form method="post" action="${doLogin}" id="loginForm" onsubmit="return validateForm($('#loginForm'), translations)">
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
					placeholder="${msgUsername}" jschecks="required;" onchange="checkInput($('#j_username'), translations)"/>
			</div>			

			<div class="form-group">
				<label class="control-label" for="password">
					<c:out value="${msgPassword}"/>
				</label> 
				<input type="password" id="j_password" name="j_password" class="form-control" 
					placeholder="${msgPassword}" jschecks="required;" onchange="checkInput($('#j_password'), translations)"/>
			</div>			

			<div class="form-group">
				<div class="checkbox">
					<label>
						<input type="checkbox" id="remember-me" name="remember-me" /><c:out value="${msgKeepLogin}"/>
					</label>
					<div class="right-aligned">
					<a class="btn btn-link" href="#"><fmt:message key="login.reissuePassword" bundle="${msg}" /></a>
					<button type="submit" class="btn btn-primary">
						<fmt:message key="login.dologin" bundle="${msg}" />
					</button>	
					</div>
				</div>					
			</div>			
		</form>			
	</div>
</div>
<%@ include file="../template/foot.jsp"%>