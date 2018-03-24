<%@page import="org.runcity.db.entity.enumeration.EventType"%>
<%@page import="javax.servlet.jsp.jstl.fmt.LocalizationContext"%>
<%@page import="java.util.TimeZone"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="org.runcity.db.entity.Event"%>
<%@ include file="../template/init.jsp"%>
<%
	Event event = (Event) pageContext.getAttribute("event", PageContext.REQUEST_SCOPE);
	LocalizationContext bundle = (LocalizationContext) pageContext.getAttribute("msg");
	SimpleDateFormat dateTimeFormat = new SimpleDateFormat(bundle.getResourceBundle().getString("common.shortTimeStampFormat"));
	dateTimeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
%>
<div id="${prefix}rollbackErrorHolder" class="errorHolder"></div>
<div class="row">
	<form class="form-horizontal read-form col-sm-6">
		<div class="form-group">
			<label for="eventVolunteer" class="control-label col-sm-5"><fmt:message	key="event.type" bundle="${msg}" /></label>
			<div>
				<p class="form-control-static" id="eventVolunteer"><%=bundle.getResourceBundle().getString(EventType.getDisplayName(event.getType())) %></p>
			</div>
		</div>
		<div class="form-group">
			<label for="eventVolunteer" class="control-label col-sm-5"><fmt:message	key="event.volunteer" bundle="${msg}" /></label>
			<div>
				<p class="form-control-static" id="eventVolunteer"><c:out value="${event.volunteer.consumer.credentials}"/></p>
			</div>
		</div>
		<c:if test="${not empty event.closedBy}">
			<div class="form-group">
				<label for="eventVolunteer" class="control-label col-sm-5"><fmt:message	key="event.reversedAt" bundle="${msg}" /></label>
				<div>
					<p class="form-control-static" id="eventVolunteer">
					<%=dateTimeFormat.format(event.getDateTo())%>
					</p>
				</div>
			</div>	
			<div class="form-group">
				<label for="eventVolunteer" class="control-label col-sm-5"><fmt:message	key="event.reversedBy" bundle="${msg}" /></label>
				<div>
					<p class="form-control-static" id="eventVolunteer"><c:out value="${event.closedBy.consumer.credentials}"/></p>
				</div>
			</div>		
		</c:if>
	</form>
</div>
<c:if test="${event.canDelete}">
	<spring:url value="/api/v1/teamEvent/${event.id}" var="rollbackUrl"/>
	<form:form id="${prefix}eventRollbackForm" error-holder="${prefix}rollbackErrorHolder"
		action="${rollbackUrl}" method="DELETE" related-table="${table}"
		onsubmit="return submitModalForm($('#${prefix}eventRollbackForm'), event)">
		<div>
			<button type="submit" class="btn btn-danger loader-right">
				<fmt:message key="jsp.volunteer.rollbackEvent" bundle="${msg}" />
			</button>
		</div>
	</form:form>
</c:if>