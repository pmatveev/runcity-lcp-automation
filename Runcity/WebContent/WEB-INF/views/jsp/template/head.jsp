<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle basename="org.runcity.resources.i18n.main" var="msg" />

<sec:csrfMetaTags />

<spring:url value="/resources/css/bootstrap.min.css" var="bootstrapCss" />
<spring:url value="/resources/css/bootstrap-theme.min.css" var="bootstrapThemeCss" />
<spring:url value="/resources/css/runcity.css" var="runcityCss" />
<spring:url value="/resources/js/jquery.min.js" var="jqueryJs" />
<spring:url value="/resources/js/bootstrap.min.js" var="bootstrapJs" />
<spring:url value="/resources/js/runcity.js" var="runcityJs" />

<link rel='stylesheet' href='${bootstrapCss}'></link>
<link rel='stylesheet' href='${bootstrapThemeCss}'></link>
<link rel='stylesheet' href='${runcityCss}'></link>
<script src="${jqueryJs}"></script>
<script src="${bootstrapJs}"></script>
<script src="${runcityJs}"></script>

<title><fmt:message key="common.title" bundle="${msg}" /></title>
</head>
<script>
	var translations = {
		required : '<fmt:message key="js.required" bundle="${msg}" />',
		passwordStrength : '<fmt:message key="js.passwordStrength" bundle="${msg}" />',
		passwordMatch : '<fmt:message key="js.passwordMatch" bundle="${msg}" />',
		invalidEmail : '<fmt:message key="js.invalidEmail" bundle="${msg}" />',
		minLen : '<fmt:message key="js.minLength" bundle="${msg}" />',
		maxLen : '<fmt:message key="js.maxLength" bundle="${msg}" />',
		ajaxErr : '<fmt:message key="ajax.error" bundle="${msg}" />',
		ajaxHang : '<fmt:message key="ajax.hanging" bundle="${msg}" />',
		reload : '<fmt:message key="common.reload" bundle="${msg}" />'
	}
	
	var popupForms = [];
	

	$(document).ready(function() {
		popupForms.forEach(function(item, index, array) {
			$('#modal_' + item).on('shown.bs.modal', function(e) {
				afterOpenModal($('#' + item));
			});
			$('#modal_' + item).on('hide.bs.modal', function(e) {
				return beforeCloseModal($('#' + item));
			});
		});
	});
</script>
<body>
	<nav class="navbar navbar-inverse">
		<div class="container-fluid">
			<div class="navbar-header">
				<p class="navbar-brand">Runcity</p>
			</div>
			<ul class="nav navbar-nav">
			</ul>
			<ul class="nav navbar-nav navbar-right">
				<spring:url value="/register" var="goRegister"/>
				<spring:url value="/login" var="goLogin"/>
				<spring:url value="/logout" var="goLogout"/>
				<sec:authorize access="!isAuthenticated()">
					<li><a href="${goRegister}" role="button"><span class="glyphicon glyphicon-user"></span> <fmt:message key="register.header" bundle="${msg}" /></a></li>
					<li><a href="${goLogin}" role="button"><span class="glyphicon glyphicon-log-in"></span> <fmt:message key="login.header" bundle="${msg}" /></a></li>
				</sec:authorize>
				<sec:authorize access="isAuthenticated()">
					<li>
						<a class="dropdown-toggle" data-toggle="dropdown" href="#">
							<span class="glyphicon glyphicon-user"></span> 
							<sec:authentication property="principal.credentials" />
							<span class="caret"></span>
						</a>
						<ul class="dropdown-menu">
							<li>
								<a data-toggle="modal" data-target="#modal_${changePasswordByPasswordForm.htmlId}" onclick="beforeOpenModal($('#${changePasswordByPassword.htmlId}'))" href="#">
									<fmt:message key="changePassword.header" bundle="${msg}" />
								</a>
								<a data-toggle="modal" data-target="#modal_${consumerSelfEditForm.htmlId}" onclick="beforeOpenModalFetch($('#${consumerSelfEditForm.htmlId}'), null)" href="#">
									<fmt:message key="common.edit" bundle="${msg}" />
								</a>
							</li>
						</ul>
					</li>
					<li><a href="${goLogout}" role="button"><span class="glyphicon glyphicon-log-out"></span> <fmt:message key="common.logout" bundle="${msg}" /></a></li>
				</sec:authorize>
			</ul>
		</div>
	</nav>
	<!-- Modals -->
	<sec:authorize access="isAuthenticated()">
			<c:set value="${true}" var="modal"/>
			<%@ include file="../forms/changePasswordByPasswordForm.jsp"%>
			<%@ include file="../forms/consumerSelfEditForm.jsp"%>
	</sec:authorize>