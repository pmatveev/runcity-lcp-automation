<%@ include file="../template/head.jsp"%>
<div class="container">
	<c:set value="${true}" var="modal"/>
	<c:set value="${gameTable}" var="currTable"/>
	<%@ include file="../forms/gameCreateEditForm.jsp"%>
	<runcity:table bundle="${msg}" table="${gameTable}"/>
</div>
<%@ include file="../template/foot.jsp"%>