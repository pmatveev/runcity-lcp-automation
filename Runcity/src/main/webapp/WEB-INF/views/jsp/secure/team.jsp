<%@ include file="../template/head.jsp"%>
<div class="container">
	<h1><c:out value="${team.number} ${team.name}"/></h1>
	<%@ include file="../sub/team.jsp"%>
</div>
<%@ include file="../template/foot.jsp"%>