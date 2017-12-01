<%@ include file="../template/head.jsp"%>
<div class="container">
	<c:set value="${true}" var="modal"/>
	<c:set value="${null}" var="currTable"/>
	<%@ include file="../forms/changePasswordByIdForm.jsp"%>
	<c:set value="${consumerTable}" var="currTable"/>
	<%@ include file="../forms/consumerCreateForm.jsp"%>
	<%@ include file="../forms/consumerEditForm.jsp"%>
	<runcity:table bundle="${msg}" table="${consumerTable}"/>
</div>
<%@ include file="../template/foot.jsp"%>