<%@page import="javax.servlet.jsp.jstl.fmt.LocalizationContext"%>
<%@page import="org.runcity.db.entity.enumeration.TeamStatus"%>
<%@page import="org.runcity.util.StringUtils"%>
<%@page import="org.runcity.db.entity.Team"%>
<%@ include file="../template/init.jsp"%>

<ul class="nav nav-tabs">
	<li class="active"><a data-toggle="tab" href="#${prefix}teaminfo"><fmt:message key="jsp.team.infoTab" bundle="${msg}" /></a></li>
	<li><a data-toggle="tab" href="#${prefix}teamhistory"><fmt:message key="${teamEventTable.simpleTitle}" bundle="${msg}" /></a></li>
</ul>

<c:if test="${coordinator}">
	<div id="${prefix}modalForms" class="div-modal">
		<c:set value="${true}" var="modal"/>
		<%@ include file="../forms/teamSetStatusByCoordinatorForm.jsp"%>
	</div>
</c:if>
<%
	Team team = (Team) pageContext.getAttribute("team", PageContext.REQUEST_SCOPE);
	LocalizationContext bundle = (LocalizationContext) pageContext.getAttribute("msg");
%>
<div class="tab-content">
	<div id="${prefix}teaminfo" class="tab-pane active">
		<c:if test="${coordinator}">
			<div id="${prefix}errorHolder" class="errorHolder"></div>
			<div class="row">
				<div class="col-sm-12">
					<button class="btn btn-default" data-toggle="modal" data-target="#modal_${teamSetStatusByCoordinatorForm.htmlId}" 
						onclick="beforeOpenModal($('#${teamSetStatusByCoordinatorForm.htmlId}'), null)">
						<fmt:message key="${teamSetStatusByCoordinatorForm.title}" bundle="${msg}" />
					</button>
					
					<spring:url value="/api/v1/team/active" var="activeUrl"/>
					<form:form id="${prefix}teamActiveForm" class="form-inline"
						action="${activeUrl}" method="POST" error-holder="${prefix}errorHolder"
						onsubmit="return submitModalForm($('#${prefix}teamActiveForm'), event)">
						<input class="preserve-value" type="hidden" name="teamId" value="${team.id}"/>
						<div>
							<button type="submit" class="btn btn-default">
								<fmt:message key="coordinator.teamVerify" bundle="${msg}" />
							</button>
						</div>
					</form:form>
				</div>
			</div>
		</c:if>
		<c:if test="${not empty prefix}">
			<spring:url value="/secure/team/${team.id}" var="teamLink"/>
			<a class="pull-right" href="${teamLink}"><span class="glyphicon glyphicon-link"></span><fmt:message key="common.permLink" bundle="${msg}" /></a>
		</c:if>
		<form class="form-horizontal read-form row">
			<div class="col-sm-6">
				<div class="form-group">
					<label for="eventVolunteer" class="control-label col-sm-5"><fmt:message	key="team.category" bundle="${msg}" /></label>
					<div>
						<p class="form-control-static" id="eventVolunteer">
						<%=StringUtils.xss(team.getRoute().getCategory().getLocalizedName(team.getRoute().getGame().getLocale())) %>
						</p>
					</div>
				</div>
				<div class="form-group">
					<label for="eventVolunteer" class="control-label col-sm-5"><fmt:message	key="team.number" bundle="${msg}" /></label>
					<div>
						<p class="form-control-static" id="eventVolunteer"><c:out value="${team.number}"/></p>
					</div>
				</div>
				<div class="form-group">
					<label for="eventVolunteer" class="control-label col-sm-5"><fmt:message	key="team.name" bundle="${msg}" /></label>
					<div>
						<p class="form-control-static" id="eventVolunteer"><c:out value="${team.name}"/></p>
					</div>
				</div>
			</div>
			<div class="col-sm-6">
				<div class="form-group">
					<label for="eventVolunteer" class="control-label col-sm-5"><fmt:message	key="team.status" bundle="${msg}" /></label>
					<div>
						<p class="form-control-static" id="eventVolunteer"><%=StringUtils.xss(TeamStatus.getDisplayName(team.getStatusData(), bundle)) %></p>
					</div>
				</div>
				<c:if test="${coordinator}">
					<div class="form-group">
						<label for="eventVolunteer" class="control-label col-sm-5"><fmt:message	key="team.contact" bundle="${msg}" /></label>
						<div>
							<p class="form-control-static" id="eventVolunteer"><c:out value="${team.contact}"/></p>
						</div>
					</div>
					<div class="form-group">
						<label for="eventVolunteer" class="control-label col-sm-5"><fmt:message	key="team.addData" bundle="${msg}" /></label>
						<div>
							<p class="form-control-static" id="eventVolunteer"><c:out value="${team.addData}"/></p>
						</div>
					</div>
				</c:if>
			</div>
		</form>
	</div>
	<div id="${prefix}teamhistory" class="tab-pane">
		<runcity:table bundle="${msg}" table="${teamEventTable}" caption="false"/>
	</div>
</div>