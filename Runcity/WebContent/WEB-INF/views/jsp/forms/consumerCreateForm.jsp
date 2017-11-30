<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="runcity" uri="/WEB-INF/runcity.tld"%>

<c:set value="${consumerCreateForm}" var="formVar"/>
<runcity:form bundle="${msg}" modal="${modal}" form="${formVar}" relatedTable="${currTable}">	
	<runcity:form-body modal="${modal}">
	<div class="errorHolder">
		<form:errors cssClass="alert alert-danger" element="div"/>
	</div>
	
	<c:set value="${formVar.credentialsColumn}" var="col"/>
	<spring:bind path="${col.name}">
		<runcity:input bundle="${msg}" column="${col}" status="${status.error}" autofocus="autofocus"/>
	</spring:bind>		
	
	<c:set value="${formVar.usernameColumn}" var="col"/>
	<spring:bind path="${col.name}">
		<runcity:input bundle="${msg}" column="${col}" status="${status.error}" />	
	</spring:bind>

	<c:set value="${formVar.passwordColumn}" var="col"/>
	<spring:bind path="${col.name}">
		<runcity:input bundle="${msg}" column="${col}" status="${status.error}"/>		
	</spring:bind>

	<c:set value="${formVar.password2Column}" var="col"/>
	<spring:bind path="${col.name}">
		<runcity:input bundle="${msg}" column="${col}" status="${status.error}"/>		
	</spring:bind>	

	<c:set value="${formVar.emailColumn}" var="col"/>
	<spring:bind path="${col.name}">
		<runcity:input bundle="${msg}" column="${col}" status="${status.error}" />
	</spring:bind>
	
	<c:set value="${formVar.activeColumn}" var="col"/>
	<spring:bind path="${col.name}">
		<runcity:input bundle="${msg}" column="${col}" status="${status.error}" />
	</spring:bind>	
	
	<c:set value="${formVar.rolesColumn}" var="col"/>
	<spring:bind path="${col.name}">
		<runcity:input bundle="${msg}" column="${col}" status="${status.error}" />
	</spring:bind>	
	
	</runcity:form-body>
	<runcity:form-footer modal="${modal}">
	
	<div class="form-group">
		<button type="submit" class="btn btn-primary">
			<fmt:message key="common.submitForm" bundle="${msg}" />
		</button>		
		<c:if test="${modal}">
			<button type="button" class="btn btn-link" data-dismiss="modal">
				<fmt:message key="common.closeModal" bundle="${msg}" />
			</button>
		</c:if>
	</div>
	</runcity:form-footer>
</runcity:form>