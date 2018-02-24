<%@ include file="../template/init.jsp"%>

<ul class="nav nav-tabs">
	<li><a data-toggle="tab" href="#${prefix}volTab"><fmt:message key="${volunteerTableByGame.simpleTitle}" bundle="${msg}" /></a></li>
	<li><a data-toggle="tab" href="#${prefix}coordTab"><fmt:message key="${coordinatorTableByGame.simpleTitle}" bundle="${msg}" /></a></li>
</ul>

<div id="${prefix}modalForms" class="div-modal">
	<c:set value="${true}" var="modal"/>
	<c:set value="${volunteerTableByGame}" var="currTable"/>
	<%@ include file="../forms/volunteerCreateEditByGameCP.jsp"%>
	<c:set value="${coordinatorTableByGame}" var="currTable"/>
	<%@ include file="../forms/volunteerCreateEditByGameCoord.jsp"%>
</div>

<div class="tab-content">
	<div id="${prefix}volTab" class="tab-pane">
		<runcity:table bundle="${msg}" table="${volunteerTableByGame}" caption="false"/>
	</div>
	<div id="${prefix}coordTab" class="tab-pane">
		<runcity:table bundle="${msg}" table="${coordinatorTableByGame}" caption="false"/>
	</div>
</div>