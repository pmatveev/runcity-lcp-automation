<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="runcity" uri="/WEB-INF/runcity.tld"%>

<c:set value="${changePasswordByTokenForm}" var="formVar"/>
<h1><fmt:message key="${formVar.title}" bundle="${msg}" /></h1>
<spring:url value="${formVar.urlOnSubmit }" var="doSubmit" />
<form:form id="${formVar.htmlId}" method="POST" modelAttribute="${formVar.formName}" action="${doSubmit}" onsubmit="return validateForm($('#${formVar.htmlId}'), event)">	
	<runcity:form-body modal="${modal}">
		<div class="errorHolder">
			<form:errors cssClass="alert alert-danger" element="div"/>
		</div>
		
		<c:set value="${formVar.tokenColumn}" var="col"/>
		<spring:bind path="${col.name}">
			<runcity:input bundle="${msg}" column="${col}" status="${status.error}"/>	
		</spring:bind>
		
		<c:set value="${formVar.checkColumn}" var="col"/>
		<spring:bind path="${col.name}">
			<runcity:input bundle="${msg}" column="${col}" status="${status.error}"/>	
		</spring:bind>

		<c:set value="${formVar.passwordColumn}" var="col"/>
		<spring:bind path="${col.name}">
			<runcity:input bundle="${msg}" column="${col}" status="${status.error}" autofocus="autofocus"/>	
		</spring:bind>

		<c:set value="${formVar.password2Column}" var="col"/>
		<spring:bind path="${col.name}">
			<runcity:input bundle="${msg}" column="${col}" status="${status.error}"/>		
		</spring:bind>	
	</runcity:form-body>
	<runcity:form-footer bundle="${msg}" modal="${modal}"/>
</form:form>