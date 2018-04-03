<%@ include file="../template/head.jsp"%>
<spring:url value="/j_spring_security_check" var="doLogin" />
<fmt:message key="login.username" bundle="${msg}" var="msgUsername"/>
<fmt:message key="user.password" bundle="${msg}" var="msgPassword"/>
<fmt:message key="login.keepLogin" bundle="${msg}" var="msgKeepLogin"/>
<div class="container form-container narrow static-margin-top">
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
			<c:if test="${!empty info}">
				<fmt:message key="${info}" bundle="${msg}" var="errorMsg"/>
				<div class="alert alert-success">
					${errorMsg}
				</div>
			</c:if>
		</div>
		<div class="form-group">
			<label class="control-label" for="username">
				<c:out value="${msgUsername}"/>
			</label> 
			<input type="text" id="j_username" name="j_username"  class="form-control" autofocus="autofocus"
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
		
		<a class="btn btn-link btn-block" data-toggle="modal" data-target="#modal_${consumerRegisterForm.htmlId}" href="#"
			onclick="beforeOpenModal($('#${consumerRegisterForm.htmlId}'), false)">
			<fmt:message key="login.register" bundle="${msg}" />
		</a>
		
		<a class="btn btn-link btn-block" data-toggle="modal" data-target="#modal_${passwordRecoveryForm.htmlId}" href="#"
			onclick="beforeOpenModal($('#${passwordRecoveryForm.htmlId}'), false)">
			<fmt:message key="login.recoverPassword" bundle="${msg}" />
		</a>
	</form>			
</div>
<c:set value="${true}" var="modal"/>
<%@ include file="../forms/passwordRecoveryForm.jsp"%>
<%@ include file="../forms/consumerRegisterForm.jsp"%>
	
<%@ include file="../template/foot.jsp"%>