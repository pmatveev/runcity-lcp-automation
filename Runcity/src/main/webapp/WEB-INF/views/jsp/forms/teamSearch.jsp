<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="runcity" uri="/WEB-INF/runcity.tld"%>
<div class="narrow">
	<spring:url value="/secure/team" var="searchUrl"/>
	<form:form method="GET" action="${searchUrl}">
           <div class="form-group">
           	<input type="hidden" name="game" value="${searchGameId}"/>
               <div class="input-group">
                   <input id="teamsearch" name="number" type="text" class="form-control" placeholder="<fmt:message key="team.number" bundle="${msg}" />"/>
                   <span class="input-group-btn">
                    <button class="btn btn-default" type="submit">
                       	<span class="glyphicon glyphicon-search"></span>
                       </button>
                   </span>
               </div>
           </div>	
		<runcity:keyboard bundle="${msg}" for="teamsearch" 
			rows="${'keyboard.team.row1,keyboard.team.row2,keyboard.team.row3,keyboard.team.row4,keyboard.team.row5'}"/>
	</form:form>
</div>