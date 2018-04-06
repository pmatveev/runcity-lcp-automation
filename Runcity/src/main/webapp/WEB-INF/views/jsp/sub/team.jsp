<%@page import="org.springframework.web.servlet.tags.form.TagWriter"%>
<%@page import="org.runcity.db.entity.RouteItem"%>
<%@page import="java.util.Comparator"%>
<%@page import="java.util.Collections"%>
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
		<form class="form-horizontal read-form">
			<div class="row">
				<div class="col-sm-12">
					<h3><fmt:message key="jsp.team.information" bundle="${msg}" /></h3>
				</div>
			</div>
			<c:if test="${not empty prefix}">
				<div class="row">
					<div class="col-sm-6">
						<spring:url value="/secure/team/${team.id}" var="teamLink"/>
						<a href="${teamLink}"><span class="glyphicon glyphicon-link"></span><fmt:message key="common.permLink" bundle="${msg}" /></a>
					</div>
				</div>
			</c:if>
			<div class="row">
				<div class="col-sm-6">
					<div class="form-group">
						<label for="eventVolunteer" class="control-label col-sm-5"><fmt:message key="team.category" bundle="${msg}" /></label>
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
					<c:if test="${not (team.status == TeamStatus.ACTIVE) }">
						<div class="form-group">
							<label for="eventVolunteer" class="control-label col-sm-5"><fmt:message	key="team.status" bundle="${msg}" /></label>
							<div>
								<p class="form-control-static" id="eventVolunteer"><%=StringUtils.xss(TeamStatus.getDisplayName(team.getStatusData(), bundle)) %></p>
							</div>
						</div>
					</c:if>
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
			</div>
			<div class="row">
				<div class="col-sm-12">
					<h3><fmt:message key="jsp.team.route" bundle="${msg}" /></h3>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-12 team-route">
					<%
						TagWriter tagWriter = new TagWriter(pageContext);
						Collections.sort(team.getRoute().getRouteItems(), new Comparator<RouteItem>() {
							@Override
							public int compare(RouteItem o1, RouteItem o2) {
								return new Integer(o1.getSortIndex()).compareTo(o2.getSortIndex());
							}
						});
					
						tagWriter.startTag("div");
						tagWriter.writeAttribute("class", "visible-xs");
						for (int i = 0; i < team.getRoute().getRouteItems().size(); i++) {
							if (i > 0) {
								tagWriter.startTag("span");
								tagWriter.writeAttribute("class", "glyphicon glyphicon-arrow-down");
								tagWriter.appendValue("");
								tagWriter.endTag();
								tagWriter.startTag("br");
								tagWriter.endTag();
							}
							
							RouteItem ri = team.getRoute().getRouteItems().get(i);
							String cls;
							if (team.getLeg() == null || team.getLeg() > ri.getSafeLegIndex()) {
								cls = "label label-default";
							} else if (team.getLeg() == ri.getSafeLegIndex()) {
								cls = "label label-success";
							} else {
								cls = "label label-info";
							}
							tagWriter.startTag("span");
							tagWriter.writeAttribute("class", cls);
							tagWriter.appendValue(ri.getControlPoint().getNameDisplay());
							tagWriter.endTag();
							tagWriter.startTag("br");
							tagWriter.endTag();
						}
						tagWriter.endTag();
					
						tagWriter.startTag("div");
						tagWriter.writeAttribute("class", "hidden-xs");
						for (int i = 0; i < team.getRoute().getRouteItems().size(); i++) {
							if (i > 0) {
								tagWriter.appendValue(" ");
								tagWriter.startTag("span");
								tagWriter.writeAttribute("class", "glyphicon glyphicon-arrow-right");
								tagWriter.appendValue("");
								tagWriter.endTag();
								tagWriter.appendValue(" ");
							}
							RouteItem ri = team.getRoute().getRouteItems().get(i);
							String cls;
							if (team.getLeg() == null || team.getLeg() > ri.getSafeLegIndex()) {
								cls = "label label-default";
							} else if (team.getLeg() == ri.getSafeLegIndex()) {
								cls = "label label-success";
							} else {
								cls = "label label-info";
							}
							tagWriter.startTag("span");
							tagWriter.writeAttribute("class", cls);
							tagWriter.appendValue(ri.getControlPoint().getNameDisplay());
							tagWriter.endTag();
						}
						tagWriter.endTag();
					%>
				</div>
			</div>
		</form>
	</div>
	<div id="${prefix}teamhistory" class="tab-pane">
		<runcity:table bundle="${msg}" table="${teamEventTable}" caption="false"/>
	</div>
</div>