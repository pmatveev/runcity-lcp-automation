<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<div id="modal_about" class="modal" role="dialog" style="display: none;">
   <div class="modal-dialog">
      <div class="modal-content">
         <div class="modal-header">
            <h4 class="modal-title"><fmt:message key="common.about" bundle="${msg}" /></h4>
         </div>
         <div class="modal-body">
         	<table width="100%" class="info-table">
         		<tr>
         			<td><b><fmt:message key="common.version" bundle="${msg}" /></b></td>
         			<td>${version.version}</td>
         		</tr>
         		<tr>
         			<td><b><fmt:message key="common.credits" bundle="${msg}" /></b></td>
         			<td>
         				<fmt:message key="common.credits.coord.KT" bundle="${msg}" /><br/>
         				<fmt:message key="common.credits.arch.PM" bundle="${msg}" /><br/>
         				<fmt:message key="common.credits.ba.OK" bundle="${msg}" /><br/>
         				<fmt:message key="common.credits.sa.AZ" bundle="${msg}" /><br/>
         				<fmt:message key="common.credits.dev.PM" bundle="${msg}" /><br/>
         				<fmt:message key="common.credits.dev.VA" bundle="${msg}" /><br/>
         				<fmt:message key="common.credits.loc.VK" bundle="${msg}" />
         			</td>
         		</tr>
         	</table>
         </div>
      </div>
   </div>
</div>