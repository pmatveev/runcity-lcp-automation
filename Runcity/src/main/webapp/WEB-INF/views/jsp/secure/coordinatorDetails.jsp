<%@ include file="../template/head.jsp"%>
<div class="container">
	<h1>
		<fmt:message key="jsp.coordinator.header" bundle="${msg}">
			<fmt:param value="${volunteer.game.name}"/>
		</fmt:message>
	</h1>
	<ul class="nav nav-tabs">
		<li class="active"><a data-toggle="tab" href="#volunteerstab"><fmt:message key="${coordControlPointTable.simpleTitle}" bundle="${msg}" /></a></li>
		<li><a data-toggle="tab" href="#statisticstab"><fmt:message key="${coordTeamStatTable.simpleTitle}" bundle="${msg}" /></a></li>
		<li><a data-toggle="tab" href="#teamsearchtab"><fmt:message key="jsp.volunteer.searchTeam" bundle="${msg}" /></a></li>
	</ul>

	<div class="tab-content">
		<div id="volunteerstab" class="tab-pane active">
			<c:set value="${true}" var="modal"/>
			<c:set value="${coordControlPointTable}" var="currTable"/>
			<%@ include file="../forms/volunteerCreateEditByGameCP.jsp"%>
			<runcity:table bundle="${msg}" table="${coordControlPointTable}" caption="false"/>
		</div>
		<div id="statisticstab" class="tab-pane">
			<c:set value="${true}" var="modal"/>
			<c:set value="${coordTeamStatTable}" var="currTable"/>
			<%@ include file="../forms/teamNotStartedByCoordinatorForm.jsp"%>
			<%@ include file="../forms/teamFinishByCoordinatorForm.jsp"%>
			<%@ include file="../forms/teamRetireByCoordinatorForm.jsp"%>
			<%@ include file="../forms/teamDisqualifyByCoordinatorForm.jsp"%>
			<runcity:table bundle="${msg}" table="${coordTeamStatTable}" caption="false"/>
		</div>
		<div id="teamsearchtab" class="tab-pane">
			<c:set var="searchGameId" value="${volunteer.game.id}"/>
			<%@ include file="../forms/teamSearch.jsp"%>		
		</div>
	</div>
</div>

<%@ include file="../template/foot.jsp"%>