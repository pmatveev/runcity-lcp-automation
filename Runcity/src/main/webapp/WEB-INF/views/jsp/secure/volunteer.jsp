<%@ include file="../template/head.jsp"%>
<div class="container">
	<h1><fmt:message key="jsp.volunteer.header" bundle="${msg}" /></h1>
	<div class="tabbable">
		<ul class="nav nav-tabs">
			<li class="active"><a data-toggle="tab" href="#volTab"><fmt:message key="jsp.volunteer.lcpTab" bundle="${msg}" /></a></li>
			<li><a data-toggle="tab" href="#coordTab"><fmt:message key="jsp.volunteer.coordinatorTab" bundle="${msg}" /></a></li>
		</ul>
		
		<div class="tab-content">
			<div id="volTab" class="tab-pane active">
				<runcity:volunteer bundle="${msg}" data="${volunteerData}"/>
			</div>
			<div id="coordTab" class="tab-pane">
				<runcity:volunteer bundle="${msg}" data="${coordinatorData}"/>
			</div>
		</div>
	</div>
</div>
<%@ include file="../template/foot.jsp"%>