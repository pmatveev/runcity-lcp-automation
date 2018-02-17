<%@ include file="../template/head.jsp"%>
<div class="container">
	<c:set value="${true}" var="modal"/>
	<c:set value="${routeTable}" var="currTable"/>
	<%@ include file="../forms/routeCreateForm.jsp"%>
	<runcity:table bundle="${msg}" table="${routeTable}"/>
</div>
<%@ include file="../template/foot.jsp"%>