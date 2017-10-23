<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<fmt:setBundle basename="org.runcity.resources.i18n.main" var="msg" />

<link rel='stylesheet' href='/Runcity/css/bootstrap.min.css'></link>
<link rel='stylesheet' href='/Runcity/css/bootstrap-theme.min.css'></link>
<link rel='stylesheet' href='/Runcity/css/runcity.css'></link>
<script src="/Runcity/lib/jquery.min.js"></script>
<script src="/Runcity/lib/bootstrap.min.js"></script>
<script src="/Runcity/lib/runcity.js"></script>

<title><fmt:message key="common.title" bundle="${msg}" /></title>
</head>
<script>
	var translations = {
		required : '<fmt:message key="js.required" bundle="${msg}" />',
		passwordStrength : '<fmt:message key="js.passwordStrength" bundle="${msg}" />',
		passwordMatch : '<fmt:message key="js.passwordMatch" bundle="${msg}" />'
	}
</script>