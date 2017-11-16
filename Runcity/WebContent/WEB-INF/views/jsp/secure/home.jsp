<%@ include file="../template/head.jsp"%>
<div class="container">
	<table id="usersTable"
		class="datatables table table-striped table-bordered"
		ajaxSource="/Runcity/api/v1/consumerTable"
		ajaxButtonSource="/Runcity/api/v1/consumerTableButtons">
		<thead>
			<tr>
				<th mapping="id">id</th>
				<th mapping="username"><fmt:message key="user.username" bundle="${msg}" /></th>
				<th mapping="credentials"><fmt:message key="user.credentials" bundle="${msg}" /></th>
				<th mapping="email"><fmt:message key="user.email" bundle="${msg}" /></th>
				<th mapping="active"><fmt:message key="user.active" bundle="${msg}" /></th>
				<th mapping="roles"><fmt:message key="user.roles" bundle="${msg}" /></th>
			</tr>
		</thead>
	</table>
</div>
<%@ include file="../template/foot.jsp"%>