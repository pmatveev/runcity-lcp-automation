<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<fmt:setBundle basename="i18n.main" var="msg" />

<sec:csrfMetaTags />
<c:set value="${pageContext.response.locale.language}" var="lang"/>
<spring:url value="/resources/css/bootstrap.min.css" var="bootstrapCss" />
<spring:url value="/resources/css/bootstrap-theme.min.css" var="bootstrapThemeCss" />
<spring:url value="/resources/css/bootstrap-select.min.css" var="bootstrapSelectCss" />
<spring:url value="/resources/css/bootstrap-datetimepicker.min.css" var="bootstrapDateCss" />
<spring:url value="/resources/css/bootstrap-colorpicker.min.css" var="bootstrapColorCss" />
<spring:url value="/resources/css/dataTables.bootstrap.min.css" var="dtCss" />
<spring:url value="/resources/css/buttons.bootstrap.min.css" var="dtButtonsCss" />
<spring:url value="/resources/css/fixedHeader.bootstrap.min.css" var="dtFixedHeaderCss" />
<spring:url value="/resources/css/select.bootstrap.min.css" var="dtSelectCss" />
<spring:url value="/resources/css/runcity.css" var="runcityCss" />
<spring:url value="/resources/js/jquery.min.js" var="jqueryJs" />
<spring:url value="/resources/js/bootstrap.min.js" var="bootstrapJs" />
<spring:url value="/resources/js/bootbox.min.js" var="bootboxJs" />
<spring:url value="/resources/js/bootstrap-select.min.js" var="bootstrapSelectJs" />
<spring:url value="/resources/js/bootstrap-datetimepicker.min.js" var="bootstrapDateJs" />
<spring:url value="/resources/js/bootstrap-colorpicker.min.js" var="bootstrapColorJs" />
<spring:url value="/resources/js/jquery.dataTables.min.js" var="dtMainJs" />
<spring:url value="/resources/js/dataTables.bootstrap.min.js" var="dtJs" />
<spring:url value="/resources/js/dataTables.buttons.min.js" var="dtButtonsMainJs" />
<spring:url value="/resources/js/buttons.bootstrap.min.js" var="dtButtonsJs" />
<spring:url value="/resources/js/dataTables.fixedHeader.min.js" var="dtFinedHeaderJs" />
<spring:url value="/resources/js/dataTables.select.min.js" var="dtSelectJs" />
<spring:url value="/resources/js/runcity.js" var="runcityJs" />

<link rel='stylesheet' href='${bootstrapCss}'/>
<link rel='stylesheet' href='${bootstrapThemeCss}'/>
<link rel='stylesheet' href='${bootstrapSelectCss}'/>
<link rel='stylesheet' href='${bootstrapDateCss}'/>
<link rel='stylesheet' href='${bootstrapColorCss}'/>
<link rel='stylesheet' href='${dtCss}'/>
<link rel='stylesheet' href='${dtButtonsCss}'/>
<link rel='stylesheet' href='${dtFixedHeaderCss}'/>
<link rel='stylesheet' href='${dtSelectCss}'/>
<link rel='stylesheet' href='${runcityCss}'/>
<script type="text/javascript" src="${jqueryJs}"></script>
<script type="text/javascript" src="${bootstrapJs}"></script>
<script type="text/javascript" src="${bootboxJs}"></script>
<script type="text/javascript" src="${bootstrapSelectJs}"></script>
<script type="text/javascript" src="${bootstrapDateJs}"></script>
<script type="text/javascript" src="${bootstrapColorJs}"></script>
<script type="text/javascript" src="${dtMainJs}"></script>
<script type="text/javascript" src="${dtJs}"></script>
<script type="text/javascript" src="${dtButtonsMainJs}"></script>
<script type="text/javascript" src="${dtButtonsJs}"></script>
<script type="text/javascript" src="${dtFinedHeaderJs}"></script>
<script type="text/javascript" src="${dtSelectJs}"></script>
<script type="text/javascript" src="${runcityJs}"></script>

<c:if test="${lang != 'en'}">
	<spring:url value="/resources/i18n/bootstrap-datetimepicker.${lang}.js" var="bootstrapDateJsLoc" />
	<spring:url value="/resources/i18n/datatables.${lang}.js" var="datatablesLoc" />
	<spring:url value="/resources/i18n/bootstrap-select.${lang}.js" var="selectLoc" />
	<script type="text/javascript" src="${bootstrapDateJsLoc}" charset="UTF-8"></script>
	<script type="text/javascript" src="${selectLoc}" charset="UTF-8"></script>
</c:if>

<title><fmt:message key="common.title" bundle="${msg}" /></title>
<script type="text/javascript">
	var translations = {
		required                 : '<fmt:message key="validation.required" bundle="${msg}" />',
		allRequired              : '<fmt:message key="validation.allRequired" bundle="${msg}" />',
		oneRequired              : '<fmt:message key="validation.oneRequired" bundle="${msg}" />',
		passwordStrength         : '<fmt:message key="validation.passwordStrength" bundle="${msg}" />',
		passwordMatch            : '<fmt:message key="validation.passwordMatch" bundle="${msg}" />',
		invalidEmail             : '<fmt:message key="validation.invalidEmail" bundle="${msg}" />',
		minLen                   : '<fmt:message key="validation.minLength" bundle="${msg}" />',
		maxLen                   : '<fmt:message key="validation.maxLength" bundle="${msg}" />',
		ajaxErr                  : '<fmt:message key="ajax.error" bundle="${msg}" />',
		ajaxHangGet              : '<fmt:message key="ajax.hangingGet" bundle="${msg}" />',
		ajaxHangPost             : '<fmt:message key="ajax.hangingPost" bundle="${msg}" />',
		reload                   : '<fmt:message key="common.reload" bundle="${msg}" />',
		forbidden                : '<fmt:message key="common.forbidden" bundle="${msg}" />',
		confTitle                : '<fmt:message key="confirmation.title" bundle="${msg}" />',
		modalCancel              : '<fmt:message key="common.closeModal" bundle="${msg}" />',
		modalOK                  : '<fmt:message key="common.submitForm" bundle="${msg}" />',
		tableDateFormat          : '<fmt:message key="common.tableDateFormat" bundle="${msg}" />'
	}
	
	$(document).ready(function() {
		$(".modal").each(function() {
			initModal($(this));
		});
		$(".colorpicker-component").colorpicker({
			format : 'hex6', 
			useAlpha : false,
			useHashPrefix : false
		});
		$(".datepicker-component").each(function() {
			initDatePicker($(this), '${lang}');
		});
		$.fn.dataTable.ext.errMode = 'throw';
		$("table.datatables").each(function() {
			initDatatables($(this), '${datatablesLoc}', '${lang}');
		});
		$('.selectpicker.ajax-sourced').on('show.bs.select', function(e) {
			loadAjaxSourced($(this));
		});
	});
</script>
</head>

<body>
	<nav class="navbar navbar-inverse navbar-fixed-top">
		<div class="container-fluid">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#mainNav">
					<span class="icon-bar"></span> 
					<span class="icon-bar"></span> 
					<span class="icon-bar"></span>
				</button>
				<spring:url value="/secure/home" var="goHome"/>
				<a href="${goHome}" role="button" class="navbar-brand"><fmt:message key="common.brand" bundle="${msg}" /></a>
			</div>
			<div class="nav collapse navbar-collapse" id="mainNav">
				<sec:authorize access="isAuthenticated()">
					<ul class="nav navbar-nav">
						<sec:authorize ifAllGranted="ROLE_ADMIN">
							<spring:url value="/secure/users" var="goUsers"/>
							<spring:url value="/secure/categories" var="goCategories"/>
							<spring:url value="/secure/games" var="goGames"/>
							<li>
								<a class="dropdown-toggle" data-toggle="dropdown" role="button">
									<fmt:message key="menu.games" bundle="${msg}" />
									<span class="caret"></span>
								</a>
								<ul class="dropdown-menu">
									<li>
										<a href="${goCategories}" role="button"><fmt:message key="menu.categories" bundle="${msg}" /></a>
										<a href="${goGames}" role="button"><fmt:message key="menu.games" bundle="${msg}" /></a>
									</li>
								</ul>
							</li>
							<li><a href="${goUsers}" role="button"><fmt:message key="menu.users" bundle="${msg}" /></a></li>
						</sec:authorize>
						<sec:authorize ifAllGranted="ROLE_VOLUNTEER">
							<li><a href="#" role="button"><fmt:message key="menu.control" bundle="${msg}" /></a></li>
						</sec:authorize>
					</ul>
				</sec:authorize>
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
									<a data-toggle="modal" data-target="#modal_${changePasswordByPasswordForm.htmlId}" onclick="beforeOpenModal($('#${changePasswordByPasswordForm.htmlId}'), false)" href="#">
										<fmt:message key="changePassword.header" bundle="${msg}" />
									</a>
									<a data-toggle="modal" data-target="#modal_${consumerSelfEditForm.htmlId}" onclick="beforeOpenModalFetch($('#${consumerSelfEditForm.htmlId}'), null)" href="#">
										<fmt:message key="common.edit" bundle="${msg}" />
									</a>
									<a data-toggle="modal" data-target="#modal_about" href="#">
										<fmt:message key="common.about" bundle="${msg}" />
									</a>
								</li>
							</ul>
						</li>
						<li><a href="${goLogout}" role="button"><span class="glyphicon glyphicon-log-out"></span> <fmt:message key="common.logout" bundle="${msg}" /></a></li>
					</sec:authorize>
				</ul>
			</div>
		</div>
	</nav>
	<div class="nav-buffer"></div>
	<!-- Modals -->
	<sec:authorize access="isAuthenticated()">
		<%@ include file="about.jsp"%>
		<c:set value="${true}" var="modal"/>
		<%@ include file="../forms/changePasswordByPasswordForm.jsp"%>
		<%@ include file="../forms/consumerSelfEditForm.jsp"%>
	</sec:authorize>