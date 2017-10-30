<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<c:set value="${consumerForm}" var="formVar"/>
<h1>
	<fmt:message key="${formVar.title}" bundle="${msg}" />
</h1>
<spring:url value="/register" var="doSubmit" />
<form:form method="post" modelAttribute="${formVar.formName}" action="${doSubmit}" id="${formVar.htmlId}" onsubmit="${formVar.onSubmit}">
	<input type="hidden"  name="${_csrf.parameterName}"   value="${_csrf.token}"/>
	<form:errors cssClass="alert alert-danger" element="div"/>
	
	<c:set value="${formVar.credentials}" var="col"/>
	<fmt:message key="${col.label}" bundle="${msg}" var="label"/>
	<spring:bind path="${col.name}">
		<div class="form-group ${status.error ? 'has-error' : ''}">
			<label class="control-label" for="${col.htmlId}">
				<c:out value="${label}"/>
			</label> 
			<form:input path="${col.name}.value" id="${col.htmlId}" type="text" class="form-control" 
				placeholder="${label}" onchange="${col.onChange}" jschecks="${col.jsChecks}" />
			<form:errors path="${col.name}" class="help-block"/>
		</div>			
	</spring:bind>		
	<c:set value="${formVar.username}" var="col"/>
	<fmt:message key="${col.label}" bundle="${msg}" var="label"/>
	<spring:bind path="${col.name}">
		<div class="form-group ${status.error ? 'has-error' : ''}">
			<label class="control-label" for="${col.htmlId}">
				<c:out value="${label}"/>
			</label> 
			<form:input path="${col.name}.value" id="${col.htmlId}" type="text" class="form-control" 
			placeholder="${label}" onchange="${col.onChange}" jschecks="${col.jsChecks}" />
			<form:errors path="${col.name}" class="help-block"/>
		</div>			
	</spring:bind>

	<c:set value="${formVar.password}" var="col"/>
	<fmt:message key="${col.label}" bundle="${msg}" var="label"/>
	<spring:bind path="${col.name}">
		<div class="form-group ${status.error ? 'has-error' : ''}">
			<label class="control-label" for="${col.htmlId}">
				<c:out value="${label}"/>
			</label> 
			<form:password path="${col.name}.value" id="${col.htmlId}" class="form-control" 
				placeholder="${label}" onchange="${col.onChange}" jschecks="${col.jsChecks}" />
			<form:errors path="${col.name}" class="help-block"/>
		</div>			
	</spring:bind>

	<c:set value="${formVar.password2}" var="col"/>
	<fmt:message key="${col.label}" bundle="${msg}" var="label"/>
	<spring:bind path="${col.name}">
		<div class="form-group ${status.error ? 'has-error' : ''}">
			<label class="control-label" for="${col.htmlId}">
				<c:out value="${label}"/>
			</label> 
			<form:password path="${col.name}.value" id="${col.htmlId}" class="form-control" 
				placeholder="${label}" onchange="${col.onChange}" jschecks="${col.jsChecks}" />
			<form:errors path="${col.name}" class="help-block"/>
		</div>			
	</spring:bind>	


	<c:set value="${formVar.email}" var="col"/>
	<fmt:message key="${col.label}" bundle="${msg}" var="label"/>
	<spring:bind path="${col.name}">
		<div class="form-group ${status.error ? 'has-error' : ''}">
			<label class="control-label" for="${col.htmlId}">
				<c:out value="${label}"/>
			</label> 
			<form:input path="${col.name}.value" id="${col.htmlId}" type="text" class="form-control" 
				placeholder="${label}" onchange="${col.onChange}" jschecks="${col.jsChecks}" />
			<form:errors path="${col.name}" class="help-block"/>
		</div>			
	</spring:bind>
	<div class="form-group">
		<button type="submit" class="btn btn-primary">
			<fmt:message key="register.doregister" bundle="${msg}" />
		</button>
	</div>
</form:form>