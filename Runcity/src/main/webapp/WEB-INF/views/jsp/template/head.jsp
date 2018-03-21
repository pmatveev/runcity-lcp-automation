<%@ include file="init.jsp"%>

<!DOCTYPE html>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<sec:csrfMetaTags />
<spring:url value="/resources/css/bootstrap.min.css" var="bootstrapCss" />
<spring:url value="/resources/css/bootstrap-theme.min.css" var="bootstrapThemeCss" />
<spring:url value="/resources/css/fileinput.min.css" var="bootstrapFileCss" />
<spring:url value="/resources/css/bootstrap-select.min.css" var="bootstrapSelectCss" />
<spring:url value="/resources/css/bootstrap-datetimepicker.min.css" var="bootstrapDateCss" />
<spring:url value="/resources/css/bootstrap-colorpicker.min.css" var="bootstrapColorCss" />
<spring:url value="/resources/css/dataTables.bootstrap.min.css" var="dtCss" />
<spring:url value="/resources/css/buttons.bootstrap.min.css" var="dtButtonsCss" />
<spring:url value="/resources/css/fixedHeader.bootstrap.min.css" var="dtFixedHeaderCss" />
<spring:url value="/resources/css/select.bootstrap.min.css" var="dtSelectCss" />
<spring:url value="/resources/css/bootstrap-toggle.min.css" var="bootstrapToggleCss" />
<spring:url value="/resources/css/runcity.css" var="runcityCss" />
<spring:url value="/resources/css/runcity-keyboard.css" var="runcityKeyboardCss" />
<spring:url value="/resources/js/jquery.min.js" var="jqueryJs" />
<spring:url value="/resources/js/bootstrap.min.js" var="bootstrapJs" />
<spring:url value="/resources/js/bootbox.min.js" var="bootboxJs" />
<spring:url value="/resources/js/fileinput.min.js" var="bootstrapFileJs" />
<spring:url value="/resources/js/bootstrap-select.min.js" var="bootstrapSelectJs" />
<spring:url value="/resources/js/bootstrap-datetimepicker.min.js" var="bootstrapDateJs" />
<spring:url value="/resources/js/bootstrap-colorpicker.min.js" var="bootstrapColorJs" />
<spring:url value="/resources/js/jquery.dataTables.min.js" var="dtMainJs" />
<spring:url value="/resources/js/dataTables.bootstrap.min.js" var="dtJs" />
<spring:url value="/resources/js/dataTables.buttons.min.js" var="dtButtonsMainJs" />
<spring:url value="/resources/js/buttons.bootstrap.min.js" var="dtButtonsJs" />
<spring:url value="/resources/js/dataTables.fixedHeader.min.js" var="dtFinedHeaderJs" />
<spring:url value="/resources/js/dataTables.select.min.js" var="dtSelectJs" />
<spring:url value="/resources/js/bootstrap-notify.min.js" var="bootstrapNotifyJs" />
<spring:url value="/resources/js/bootstrap-toggle.min.js" var="bootstrapToggleJs" />
<spring:url value="/resources/js/runcity.js" var="runcityJs" />
<spring:url value="/resources/js/runcity-keyboard.js" var="runcityKeyboardJs" />

<link rel='stylesheet' href='${bootstrapCss}'/>
<link rel='stylesheet' href='${bootstrapThemeCss}'/>
<link rel='stylesheet' href='${bootstrapSelectCss}'/>
<link rel='stylesheet' href='${bootstrapFileCss}'/>
<link rel='stylesheet' href='${bootstrapDateCss}'/>
<link rel='stylesheet' href='${bootstrapColorCss}'/>
<link rel='stylesheet' href='${dtCss}'/>
<link rel='stylesheet' href='${dtButtonsCss}'/>
<link rel='stylesheet' href='${dtFixedHeaderCss}'/>
<link rel='stylesheet' href='${dtSelectCss}'/>
<link rel='stylesheet' href='${bootstrapToggleCss}'/>
<link rel='stylesheet' href='${runcityCss}'/>
<link rel='stylesheet' href='${runcityKeyboardCss}'/>
<script type="text/javascript" src="${jqueryJs}"></script>
<script type="text/javascript" src="${bootstrapJs}"></script>
<script type="text/javascript" src="${bootboxJs}"></script>
<script type="text/javascript" src="${bootstrapFileJs}"></script>
<script type="text/javascript" src="${bootstrapSelectJs}"></script>
<script type="text/javascript" src="${bootstrapDateJs}"></script>
<script type="text/javascript" src="${bootstrapColorJs}"></script>
<script type="text/javascript" src="${dtMainJs}"></script>
<script type="text/javascript" src="${dtJs}"></script>
<script type="text/javascript" src="${dtButtonsMainJs}"></script>
<script type="text/javascript" src="${dtButtonsJs}"></script>
<script type="text/javascript" src="${dtFinedHeaderJs}"></script>
<script type="text/javascript" src="${dtSelectJs}"></script>
<script type="text/javascript" src="${bootstrapNotifyJs}"></script>
<script type="text/javascript" src="${bootstrapToggleJs}"></script>
<script type="text/javascript" src="${runcityJs}"></script>
<script type="text/javascript" src="${runcityKeyboardJs}"></script>

<c:if test="${lang != 'en'}">
	<spring:url value="/resources/i18n/bootstrap-datetimepicker.${lang}.js" var="bootstrapDateJsLoc" />
	<spring:url value="/resources/i18n/datatables.${lang}.js" var="datatablesLoc" />
	<spring:url value="/resources/i18n/fileinput.${lang}.js" var="fileLoc" />
	<spring:url value="/resources/i18n/bootstrap-select.${lang}.js" var="selectLoc" />
	<script type="text/javascript" src="${bootstrapDateJsLoc}" charset="UTF-8"></script>
	<script type="text/javascript" src="${fileLoc}" charset="UTF-8"></script>
	<script type="text/javascript" src="${selectLoc}" charset="UTF-8"></script>
</c:if>
<spring:url value="/resources/i18n/http-error.${lang}.js" var="httpErrLoc" />
<script type="text/javascript" src="${httpErrLoc}" charset="UTF-8"></script>

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
		min                      : '<fmt:message key="validation.min" bundle="${msg}" />',
		max                      : '<fmt:message key="validation.max" bundle="${msg}" />',
		reload                   : '<fmt:message key="common.reload" bundle="${msg}" />',
		confTitle                : '<fmt:message key="confirmation.title" bundle="${msg}" />',
		modalCancel              : '<fmt:message key="common.closeModal" bundle="${msg}" />',
		modalOK                  : '<fmt:message key="common.submitForm" bundle="${msg}" />',
		tableDateFormat          : '<fmt:message key="common.tableDateFormat" bundle="${msg}" />',
		tableDateTimeFormat      : '<fmt:message key="common.tableDateTimeFormat" bundle="${msg}" />',
		extnTabName              : '<fmt:message key="extension.tabHeader" bundle="${msg}" />',
		refreshEmpty             : '<fmt:message key="common.refreshEmpty" bundle="${msg}" />'
	}
	
	var locale = '${lang}';
	
	var notificationTemplate = '<div data-notify="container" class="col-xs-11 col-sm-3 alert alert-{0}" role="alert">' + 
		'<span data-notify="icon"></span>' +
		'<span data-notify="title">{1}</span>' +
		'<span data-notify="message">{2}</span>' +
		'<div class="progress" data-notify="progressbar">' +
		'<div class="progress-bar progress-bar-{0}" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width: 0%;"></div>' +
		'</div>' +
		'<a href="{3}" target="{4}" data-notify="url"></a>' +
		'</div>';
	
	var notifySettings = {
		success : {
			type: "success",
			allow_dismiss: true,
			newest_on_top: true,
			placement: {
				from: "top",
				align: "right"
			},
			delay: 3000,
			mouse_over: "pause",
			template: notificationTemplate
		},
		warning : {
			type: "warning",
			allow_dismiss: true,
			newest_on_top: true,
			placement: {
				from: "top",
				align: "right"
			},
			delay: 5000,
			mouse_over: "pause",
			template: notificationTemplate			
		}
	};
	
	$(document).ready(function() {
		$.fn.dataTable.ext.errMode = 'throw';
		initHtml($("body"), '${datatablesLoc}');
	});
</script>
</head>

<body>
	<noscript>
		<style type="text/css">
		.pagecontainer {
			display: none;
		}
		</style>
		<h1><fmt:message key="common.nojs" bundle="${msg}" /></h1>
	</noscript>
	<div class="pagecontainer">
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
								<spring:url value="/secure/users" var="goUsers" />
								<spring:url value="/secure/categories" var="goCategories" />
								<spring:url value="/secure/games" var="goGames" />
								<li><a href="${goGames}" role="button"><fmt:message key="menu.games" bundle="${msg}" /></a></li>
								<li><a href="${goCategories}" role="button"><fmt:message key="menu.categories" bundle="${msg}" /></a></li>
								<li><a href="${goUsers}" role="button"><fmt:message key="menu.users" bundle="${msg}" /></a></li>
							</sec:authorize>
							<sec:authorize ifAllGranted="ROLE_VOLUNTEER">
								<spring:url value="/secure/volunteer" var="goVolunteer" />
								<li><a href="${goVolunteer}" role="button"><fmt:message key="menu.volunteer" bundle="${msg}" /></a></li>
								<c:if test="${attributes.currentCP}">
									<spring:url value="/secure/current" var="goCurrent" />
									<li><a href="${goCurrent}" role="button"><fmt:message key="menu.current" bundle="${msg}" /></a></li>							
								</c:if>
							</sec:authorize>
						</ul>
					</sec:authorize>
					<ul class="nav navbar-nav navbar-right">
						<spring:url value="/logout" var="goLogout"/>
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