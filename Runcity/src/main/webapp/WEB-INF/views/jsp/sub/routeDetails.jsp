<%@ include file="../template/init.jsp"%>

<ul class="nav nav-tabs">
	<li><a data-toggle="tab" href="#${prefix}routeTab"><fmt:message key="${routeItemTable.simpleTitle}" bundle="${msg}" /></a></li>
	<li><a data-toggle="tab" href="#${prefix}teamTab"><fmt:message key="${teamTable.simpleTitle}" bundle="${msg}" /></a></li>
</ul>

<div id="${prefix}modalForms" class="div-modal">
	<c:set value="${true}" var="modal"/>
	<c:set value="${routeItemTable}" var="currTable"/>
	<%@ include file="../forms/routeItemCreateEditByRouteForm.jsp"%>
	<c:set value="${teamTable}" var="currTable"/>
	<%@ include file="../forms/teamCreateEditByRouteForm.jsp"%>
</div>

<div class="tab-content">
	<div id="${prefix}routeTab" class="tab-pane">
		<runcity:table bundle="${msg}" table="${routeItemTable}" caption="false"/>
	</div>
	<div id="${prefix}teamTab" class="tab-pane">
		<runcity:table bundle="${msg}" table="${teamTable}" caption="false"/>
	</div>
</div>