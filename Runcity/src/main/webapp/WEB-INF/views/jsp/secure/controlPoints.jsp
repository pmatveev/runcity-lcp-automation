<%@ include file="../template/head.jsp"%>
<div class="container">
	<c:set value="${true}" var="modal"/>
	<c:set value="${controlPointTable}" var="currTable"/>
	<%@ include file="../forms/controlPointCreateEditByGameForm.jsp"%>
	<runcity:table bundle="${msg}" table="${controlPointTable}"/>
</div>
<%@ include file="../template/foot.jsp"%>