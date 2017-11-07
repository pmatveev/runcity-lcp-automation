<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<c:set value="${consumerRegisterForm}" var="formVar"/>
<c:choose>
    <c:when test="${modal}">
    	<script>
    		popupForms.push('${formVar.htmlId}');
    	</script>
		<div id="modal_${formVar.htmlId}" class="modal" role="dialog">
			<div class="modal-dialog">
				<div class="modal-content">		
					<div class="modal-header">
						<h4 class="modal-title"><fmt:message key="${formVar.title}" bundle="${msg}" /></h4>
					</div>
    </c:when>    
    <c:otherwise>
		<h1>
			<fmt:message key="${formVar.title}" bundle="${msg}" />
		</h1>
    </c:otherwise>
</c:choose>

<spring:url value="${formVar.urlOnOpenAjax}" var="doOpenAjax" />
<spring:url value="${formVar.urlOnSubmit}" var="doSubmit" />
<spring:url value="${formVar.urlOnSubmitAjax}" var="doSubmitAjax" />

<c:choose>
    <c:when test="${modal}">
    	<c:set value="${doSubmitAjax}" var="formAction"/>
    	<c:set value="${formVar.onModalSubmit}" var="onSubmit"/>
    </c:when>    
    <c:otherwise>
    	<c:set value="${doSubmit}" var="formAction"/>
    	<c:set value="${formVar.onSubmit}" var="onSubmit"/>
    </c:otherwise>
</c:choose>

<form:form method="post" modelAttribute="${formVar.formName}" action="${formAction}" id="${formVar.htmlId}" onsubmit="${onSubmit}" fetchFrom="${doOpenAjax}">	
	<c:if test="${modal}">
		<div class="modal-body">
	</c:if>	
	
	<div class="errorHolder">
		<form:errors cssClass="alert alert-danger" element="div"/>
	</div>
	
	<c:set value="${formVar.credentialsColumn}" var="col"/>
	<fmt:message key="${col.label}" bundle="${msg}" var="label"/>
	<spring:bind path="${col.name}">
		<div class="form-group ${status.error ? 'has-error' : ''}">
			<label class="control-label" for="${col.htmlId}">
				<c:out value="${label}"/>
			</label> 
			<form:input path="${col.name}" id="${col.htmlId}" type="text" class="form-control" 
				placeholder="${label}" onchange="${col.onChange}" jschecks="${col.jsChecks}" autofocus="autofocus" />
			<form:errors path="${col.name}" class="help-block"/>
		</div>			
	</spring:bind>		
	
	<c:set value="${formVar.usernameColumn}" var="col"/>
	<fmt:message key="${col.label}" bundle="${msg}" var="label"/>
	<spring:bind path="${col.name}">
		<div class="form-group ${status.error ? 'has-error' : ''}">
			<label class="control-label" for="${col.htmlId}">
				<c:out value="${label}"/>
			</label> 
			<form:input path="${col.name}" id="${col.htmlId}" type="text" class="form-control" 
			placeholder="${label}" onchange="${col.onChange}" jschecks="${col.jsChecks}" />
			<form:errors path="${col.name}" class="help-block"/>
		</div>			
	</spring:bind>

	<c:set value="${formVar.passwordColumn}" var="col"/>
	<fmt:message key="${col.label}" bundle="${msg}" var="label"/>
	<spring:bind path="${col.name}">
		<div class="form-group ${status.error ? 'has-error' : ''}">
			<label class="control-label" for="${col.htmlId}">
				<c:out value="${label}"/>
			</label> 
			<form:password path="${col.name}" id="${col.htmlId}" class="form-control" 
				placeholder="${label}" onchange="${col.onChange}" jschecks="${col.jsChecks}" />
			<form:errors path="${col.name}" class="help-block"/>
		</div>			
	</spring:bind>

	<c:set value="${formVar.password2Column}" var="col"/>
	<fmt:message key="${col.label}" bundle="${msg}" var="label"/>
	<spring:bind path="${col.name}">
		<div class="form-group ${status.error ? 'has-error' : ''}">
			<label class="control-label" for="${col.htmlId}">
				<c:out value="${label}"/>
			</label> 
			<form:password path="${col.name}" id="${col.htmlId}" class="form-control" 
				placeholder="${label}" onchange="${col.onChange}" jschecks="${col.jsChecks}" />
			<form:errors path="${col.name}" class="help-block"/>
		</div>			
	</spring:bind>	


	<c:set value="${formVar.emailColumn}" var="col"/>
	<fmt:message key="${col.label}" bundle="${msg}" var="label"/>
	<spring:bind path="${col.name}">
		<div class="form-group ${status.error ? 'has-error' : ''}">
			<label class="control-label" for="${col.htmlId}">
				<c:out value="${label}"/>
			</label> 
			<form:input path="${col.name}" id="${col.htmlId}" type="text" class="form-control" 
				placeholder="${label}" onchange="${col.onChange}" jschecks="${col.jsChecks}" />
			<form:errors path="${col.name}" class="help-block"/>
		</div>			
	</spring:bind>
	
	<c:if test="${modal}">
		</div>
		<div class="modal-footer">
	</c:if>
	
	<div class="form-group">
		<button type="submit" class="btn btn-primary">
			<fmt:message key="register.doregister" bundle="${msg}" />
		</button>		
		<c:if test="${modal}">
			<button type="button" class="btn btn-link" data-dismiss="modal">
				<fmt:message key="common.closeModal" bundle="${msg}" />
			</button>
			</div>
		</c:if>
	</div>
</form:form>
<c:if test="${modal}">
			</div>
		</div>
	</div>
</c:if>