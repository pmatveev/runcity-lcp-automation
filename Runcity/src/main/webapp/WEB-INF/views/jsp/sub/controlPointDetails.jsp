<%@ include file="../template/init.jsp"%>

<ul class="nav nav-tabs">
	<li><a data-toggle="tab" href="#${prefix}volTab"><fmt:message key="${volunteerTable.simpleTitle}" bundle="${msg}" /></a></li>
</ul>

<div id="${prefix}modalForms" class="div-modal">
	<c:set value="${true}" var="modal"/>
	<c:set value="${volunteerTable}" var="currTable"/>
	<%@ include file="../forms/routeItemCreateEditForm.jsp"%>
</div>

<div class="tab-content">
	<div id="${prefix}volTab" class="tab-pane">
		<runcity:table bundle="${msg}" table="${volunteerTable}" caption="false"/>
	</div>
</div>