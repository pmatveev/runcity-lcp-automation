<%@ include file="../template/init.jsp"%>

<ul class="nav nav-tabs">
	<li><a data-toggle="tab" href="#${prefix}volTab"><fmt:message key="${volunteerTableByConsumer.simpleTitle}" bundle="${msg}" /></a></li>
	<li><a data-toggle="tab" href="#${prefix}coordTab"><fmt:message key="${coordinatorTableByConsumer.simpleTitle}" bundle="${msg}" /></a></li>
</ul>

<div id="${prefix}modalForms" class="div-modal">
	<c:set value="${true}" var="modal"/>
	<c:set value="${volunteerTableByConsumer}" var="currTable"/>
	<c:set value="${coordinatorTableByConsumer}" var="currTable"/>
</div>

<div class="tab-content">
	<div id="${prefix}volTab" class="tab-pane">
		<runcity:table bundle="${msg}" table="${volunteerTableByConsumer}" caption="false"/>
	</div>
	<div id="${prefix}coordTab" class="tab-pane">
		<runcity:table bundle="${msg}" table="${coordinatorTableByConsumer}" caption="false"/>
	</div>
</div>