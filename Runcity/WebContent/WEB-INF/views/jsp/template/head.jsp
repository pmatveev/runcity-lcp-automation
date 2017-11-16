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
<spring:url value="/resources/css/bootstrap-select.min.css" var="bootstrapSelectCss" />
<spring:url value="/resources/css/dataTables.bootstrap.min.css" var="dtCss" />
<spring:url value="/resources/css/buttons.bootstrap.min.css" var="dtButtonsCss" />
<spring:url value="/resources/css/fixedHeader.bootstrap.min.css" var="dtFixedHeaderCss" />
<spring:url value="/resources/css/select.bootstrap.min.css" var="dtSelectCss" />
<spring:url value="/resources/css/runcity.css" var="runcityCss" />
<spring:url value="/resources/js/jquery.min.js" var="jqueryJs" />
<spring:url value="/resources/js/bootstrap.min.js" var="bootstrapJs" />
<spring:url value="/resources/js/bootstrap-select.min.js" var="bootstrapSelectJs" />
<spring:url value="/resources/js/jquery.dataTables.min.js" var="dtMainJs" />
<spring:url value="/resources/js/dataTables.bootstrap.min.js" var="dtJs" />
<spring:url value="/resources/js/dataTables.buttons.min.js" var="dtButtonsMainJs" />
<spring:url value="/resources/js/buttons.bootstrap.min.js" var="dtButtonsJs" />
<spring:url value="/resources/js/dataTables.fixedHeader.min.js" var="dtFinedHeaderJs" />
<spring:url value="/resources/js/dataTables.select.min.js" var="dtSelectJs" />
<spring:url value="/resources/js/runcity.js" var="runcityJs" />

<link rel='stylesheet' href='${bootstrapCss}'></link>
<link rel='stylesheet' href='${bootstrapThemeCss}'></link>
<link rel='stylesheet' href='${bootstrapSelectCss}'></link>
<link rel='stylesheet' href='${dtCss}'></link>
<link rel='stylesheet' href='${dtButtonsCss}'></link>
<link rel='stylesheet' href='${dtFixedHeaderCss}'></link>
<link rel='stylesheet' href='${dtSelectCss}'></link>
<link rel='stylesheet' href='${runcityCss}'></link>
<script type="text/javascript" src="${jqueryJs}"></script>
<script type="text/javascript" src="${bootstrapJs}"></script>
<script type="text/javascript" src="${bootstrapSelectJs}"></script>
<script type="text/javascript" src="${dtMainJs}"></script>
<script type="text/javascript" src="${dtJs}"></script>
<script type="text/javascript" src="${dtButtonsMainJs}"></script>
<script type="text/javascript" src="${dtButtonsJs}"></script>
<script type="text/javascript" src="${dtFinedHeaderJs}"></script>
<script type="text/javascript" src="${dtSelectJs}"></script>
<script type="text/javascript" src="${runcityJs}"></script>

<title><fmt:message key="common.title" bundle="${msg}" /></title>
</head>
<script type="text/javascript">
	(function (root, factory) {
		  if (typeof define === 'function' && define.amd) {
		    // AMD. Register as an anonymous module unless amdModuleId is set
		    define(["jquery"], function (a0) {
		      return (factory(a0));
		    });
		  } else if (typeof module === 'object' && module.exports) {
		    // Node. Does not work with strict CommonJS, but
		    // only CommonJS-like environments that support module.exports,
		    // like Node.
		    module.exports = factory(require("jquery"));
		  } else {
		    factory(root["jQuery"]);
		  }
		}(this, function (jQuery) {
	
		(function ($) {
		  $.fn.selectpicker.defaults = {
		    noneSelectedText: '<fmt:message key="selectpicker.noneSelectedText" bundle="${msg}" />',
		    noneResultsText: '<fmt:message key="selectpicker.noneResultsText" bundle="${msg}" />',
		    countSelectedText: '<fmt:message key="selectpicker.countSelectedText" bundle="${msg}" />',
		    maxOptionsText: ['<fmt:message key="selectpicker.maxOptionsText" bundle="${msg}" />', '<fmt:message key="selectpicker.maxOptionsTextGroup" bundle="${msg}" />'],
		    doneButtonText: '<fmt:message key="selectpicker.doneButtonText" bundle="${msg}" />',
		    selectAllText: '<fmt:message key="selectpicker.selectAllText" bundle="${msg}" />',
		    deselectAllText: '<fmt:message key="selectpicker.deselectAllText" bundle="${msg}" />',
		    multipleSeparator: '<fmt:message key="selectpicker.multipleSeparator" bundle="${msg}" />'
		  };
		})(jQuery);
		}));

	var translations = {
		required : '<fmt:message key="validation.required" bundle="${msg}" />',
		passwordStrength : '<fmt:message key="validation.passwordStrength" bundle="${msg}" />',
		passwordMatch : '<fmt:message key="validation.passwordMatch" bundle="${msg}" />',
		invalidEmail : '<fmt:message key="validation.invalidEmail" bundle="${msg}" />',
		minLen : '<fmt:message key="validation.minLength" bundle="${msg}" />',
		maxLen : '<fmt:message key="validation.maxLength" bundle="${msg}" />',
		ajaxErr : '<fmt:message key="ajax.error" bundle="${msg}" />',
		ajaxHangGet : '<fmt:message key="ajax.hangingGet" bundle="${msg}" />',
		ajaxHangPost : '<fmt:message key="ajax.hangingPost" bundle="${msg}" />',
		reload : '<fmt:message key="common.reload" bundle="${msg}" />',
		forbidden : '<fmt:message key="common.forbidden" bundle="${msg}" />'
	}
	
	$(document).ready(function() {
		$(".modal").each(function() {
			initModal($(this));
		});
		
		$(".dropdown-menu .inner").each(function() {
			sortListbox($(this));		
		});
		$.fn.dataTable.ext.errMode = 'throw';
		$("table.datatables").each(function() {
			initDatatables($(this));
		})
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
								<a data-toggle="modal" data-target="#modal_${changePasswordByPasswordForm.htmlId}" onclick="beforeOpenModal($('#${changePasswordByPasswordForm.htmlId}'))" href="#">
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