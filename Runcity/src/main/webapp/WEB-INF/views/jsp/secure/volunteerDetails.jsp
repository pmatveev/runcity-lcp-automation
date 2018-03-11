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
	<runcity:controlPoint bundle="${msg}" volunteer="${volunteer}" onsiteHandler="onsite($(this))" ajaxTarget="/api/v1/volunteerOnsite"/>
</div>
<%@ include file="../template/foot.jsp"%>