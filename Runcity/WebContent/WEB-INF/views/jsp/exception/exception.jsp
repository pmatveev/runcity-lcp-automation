<%@ include file="../template/head.jsp"%>
<h1>
	<fmt:message key="exception" bundle="${msg}" />
</h1>
<c:if test="${not empty errMsg}">
	<h2><fmt:message key="exception.message" bundle="${msg}" /> ${errMsg}</h2>
</c:if>
<%@ include file="../template/foot.jsp"%>