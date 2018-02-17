<%@ include file="../template/init.jsp"%>

<ul class="nav nav-tabs">
	<li class="active"><a data-toggle="tab" href="#${prefix}routeTab"><fmt:message key="${routeItemTable.simpleTitle}" bundle="${msg}" /></a></li>
</ul>

<div id="${prefix}modalForms" class="div-modal">
	<c:set value="${true}" var="modal"/>
	<c:set value="${gameTable}" var="currTable"/>
	<%@ include file="../forms/routeItemCreateEditForm.jsp"%>
</div>

<div class="tab-content">
	<div id="${prefix}routeTab" class="tab-pane active">
		<runcity:table bundle="${msg}" table="${routeItemTable}" caption="false"/>
	</div>
</div>