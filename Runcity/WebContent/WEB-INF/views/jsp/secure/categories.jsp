<%@ include file="../template/head.jsp"%>
<div class="container">
	<c:set value="${true}" var="modal"/>
	<c:set value="${categoryTable}" var="currTable"/>
	<%@ include file="../forms/categoryCreateEditForm.jsp"%>
	<runcity:table bundle="${msg}" table="${categoryTable}"/>
</div>
<%@ include file="../template/foot.jsp"%>