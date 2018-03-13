<%@ include file="../template/head.jsp"%>
<div class="container">
	<h1>
		<fmt:message key="jsp.coordinator.header" bundle="${msg}">
			<fmt:param value="${volunteer.game.name}"/>
		</fmt:message>
	</h1>
	<ul class="nav nav-tabs">
		<li class="active"><a data-toggle="tab" href="#volunteers"><fmt:message key="${coordControlPointTable.simpleTitle}" bundle="${msg}" /></a></li>
	</ul>

	<div class="tab-content">
		<div id="volunteers" class="tab-pane active">
			<c:set value="${true}" var="modal"/>
			<c:set value="${coordControlPointTable}" var="currTable"/>
			<%@ include file="../forms/volunteerCreateEditByGameCP.jsp"%>
			<runcity:table bundle="${msg}" table="${coordControlPointTable}" caption="false"/>
		</div>
	</div>
</div>

<%@ include file="../template/foot.jsp"%>