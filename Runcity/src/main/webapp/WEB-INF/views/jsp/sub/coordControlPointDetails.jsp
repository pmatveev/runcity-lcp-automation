<%@ include file="../template/init.jsp"%>

<ul class="nav nav-tabs">
	<li><a data-toggle="tab" href="#${prefix}volTab"><fmt:message key="${coordVolunteerTableByCP.simpleTitle}" bundle="${msg}" /></a></li>
</ul>

<div id="${prefix}modalForms" class="div-modal">
	<c:set value="${true}" var="modal"/>
	<c:set value="${coordVolunteerTableByCP}" var="currTable"/>
	<%@ include file="../forms/volunteerCreateEditByCPForm.jsp"%>
</div>

<div class="tab-content">
	<div id="${prefix}volTab" class="tab-pane">
		<runcity:table bundle="${msg}" table="${coordVolunteerTableByCP}" caption="false"/>
	</div>
</div>