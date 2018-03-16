<%@ include file="../template/head.jsp"%>
<script>
	function onsite(input) {
		var ajaxData = {
			volunteer : ${volunteer.id},
			onsite : input.prop('checked')
		};
		
		var successHandler = function(data) {
			if (data.responseClass == "INFO") { 
				location.reload();
				return true;
			}
			return false;
		}
		
		processToggleAjax(input, ajaxData, successHandler);
	}
</script>
<div class="container">
	<h1>
		<fmt:message key="jsp.controlPoint.header" bundle="${msg}">
			<fmt:param><c:out value="${volunteer.controlPoint.nameDisplayWithChildren}"/></fmt:param>
		</fmt:message>
	</h1>
	<div class="row cp-onsite-toggle-div form-group">
		<input type="checkbox" data-toggle="toggle" class="cp-onsite-toggle" data-on="<fmt:message key="jsp.controlPoint.onsiteAction" bundle="${msg}" />" 
			data-off="<fmt:message key="jsp.controlPoint.offsiteAction" bundle="${msg}" />" data-onstyle="success" data-offstyle="danger" 
			onchange="onsite($(this))" ajax-target="/Runcity/api/v1/volunteerOnsite" <c:if test="${volunteer.active}">checked</c:if> />
	</div>
	<div class="row">
		<div class="narrow div-center">
			<c:set value="${false}" var="modal"/>
			<%@ include file="../forms/teamProcessForm.jsp"%>
		</div>
	</div>
	<runcity:controlPoint bundle="${msg}" volunteer="${volunteer}"/>
</div>
<%@ include file="../template/foot.jsp"%>