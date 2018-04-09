<%@page import="java.util.TimeZone"%>
<%@page import="org.springframework.web.servlet.tags.UrlTag"%>
<%@page import="org.runcity.db.entity.Game"%>
<%@page import="org.runcity.util.StringUtils"%>
<%@page import="org.runcity.db.entity.enumeration.ControlPointType"%>
<%@page import="org.runcity.db.entity.Category"%>
<%@page import="java.util.Comparator"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.runcity.db.entity.RouteItem"%>
<%@page import="java.util.List"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="org.springframework.context.i18n.LocaleContextHolder"%>
<%@page import="org.runcity.db.entity.Volunteer"%>
<%@page import="org.runcity.db.entity.ControlPoint"%>
<%@page import="javax.servlet.jsp.jstl.fmt.LocalizationContext"%>
<%@page import="org.springframework.web.servlet.tags.form.TagWriter"%>
<%@page import="java.text.MessageFormat"%>
<%!
	private String localize(LocalizationContext bundle, String message, Object... args) {
		String result = bundle.getResourceBundle().getString(message);

		if (args == null) {
			return result;
		}

		return MessageFormat.format(result, args);
	}

	private void writeField(TagWriter tagWriter, String id, String caption, String content) throws JspException {
		tagWriter.startTag("div");
		tagWriter.writeAttribute("class", "form-group");
		tagWriter.startTag("label");
		tagWriter.writeAttribute("for", id);
		tagWriter.writeAttribute("class", "control-label col-sm-4");
		tagWriter.appendValue(caption);
		tagWriter.endTag();
		tagWriter.startTag("div");
		tagWriter.startTag("p");
		tagWriter.writeAttribute("class", "form-control-static");
		tagWriter.writeAttribute("id", id);
		tagWriter.appendValue(content);
		tagWriter.endTag();
		tagWriter.endTag();
		tagWriter.endTag();
	}
	
	private String composeTeamsUrl(PageContext pageContext, Volunteer volunteer, RouteItem routeItem) throws JspException {
		String url = "/secure/controlPoint/" + volunteer.getControlPoint().getId() + "/teams" + (routeItem == null ? "" : "?routeItem=" + routeItem.getId());
		UrlTag tag = new UrlTag();
		tag.setValue(url);
		tag.setVar("teamUrl");
		tag.setPageContext(pageContext);
		tag.doStartTag();
		tag.doEndTag();
		return (String) pageContext.getAttribute("teamUrl");
	}
	
	private void writeInfo(PageContext pageContext, Volunteer volunteer, LocalizationContext bundle, String refreshAjax) throws JspException {
		TagWriter tagWriter = new TagWriter(pageContext);
		ControlPoint controlPoint = volunteer.getControlPoint().getMain();
		String locale = LocaleContextHolder.getLocale().toString();
		SimpleDateFormat dateTimeFormat = new SimpleDateFormat(localize(bundle, "common.shortDateTimeFormat"));
		dateTimeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		
		// basic info
		tagWriter.startTag("form");
		tagWriter.writeAttribute("class", "form-horizontal read-form col-sm-6");
		tagWriter.startTag("h3");
		tagWriter.appendValue(localize(bundle, "jsp.controlPoint.general"));
		tagWriter.endTag();
		writeField(tagWriter, "address", localize(bundle, "jsp.controlPoint.address"), controlPoint.getLocalizedAddress(locale));
		writeField(tagWriter, "datefrom", localize(bundle, "jsp.volunteer.from"),
				dateTimeFormat.format(volunteer.getDateFrom()));
		writeField(tagWriter, "dateto", localize(bundle, "jsp.volunteer.to"), dateTimeFormat.format(volunteer.getDateTo()));
		tagWriter.endTag();
		
		// categories
		tagWriter.startTag("div");
		tagWriter.writeAttribute("class", "volunteer-categories col-sm-6");
		tagWriter.startTag("h3");
		tagWriter.appendValue(localize(bundle, "jsp.controlPoint.categories"));
		tagWriter.appendValue("&nbsp;");
		tagWriter.startTag("button");
		tagWriter.writeAttribute("id", "infoRefresh");
		tagWriter.writeAttribute("class", "btn btn-sm btn-default ajax-refresh loader-right");
		tagWriter.writeAttribute("onclick", "refreshPageData($(this))");
		tagWriter.writeAttribute("ajax-target", refreshAjax);
		tagWriter.appendValue(localize(bundle, "common.refresh"));
		tagWriter.endTag();
		tagWriter.endTag();

		List<RouteItem> routeItems = new ArrayList<RouteItem>(controlPoint.getRouteItems());
		for (ControlPoint ch : controlPoint.getChildren()) {
			routeItems.addAll(ch.getRouteItems());
		}
		Collections.sort(routeItems, new Comparator<RouteItem>() {
			@Override
			public int compare(RouteItem o1, RouteItem o2) {
				return o1.getRoute().getCategory().getPrefix()
						.compareTo(o2.getRoute().getCategory().getPrefix());
			}
		});

		Game game = volunteer.getVolunteerGame();
		tagWriter.startTag("a");
		tagWriter.writeAttribute("href", composeTeamsUrl(pageContext, volunteer, null));
		tagWriter.startTag("p");
		tagWriter.writeAttribute("class", "volunteer-category");
		tagWriter.appendValue(localize(bundle, "jsp.volunteer.total"));
		tagWriter.appendValue("&nbsp;");
		tagWriter.startTag("span");
		tagWriter.writeAttribute("class", "badge");
		tagWriter.writeAttribute("refreshed-by", "infoRefresh");
		tagWriter.writeAttribute("refresh-key", "routeCounter_total");
		tagWriter.endTag();
		tagWriter.endTag();
		tagWriter.endTag();
		
		for (RouteItem ri : routeItems) {
			tagWriter.startTag("a");
			tagWriter.writeAttribute("href", composeTeamsUrl(pageContext, volunteer, ri));
			tagWriter.startTag("p");
			tagWriter.writeAttribute("class", "volunteer-category");

			Category c = ri.getRoute().getCategory();

			tagWriter.appendValue(c.getBadge());
			tagWriter.appendValue("&nbsp;");
			
			switch (ri.getControlPoint().getType()) {
			case START:
			case BONUS:
			case FINISH:
				tagWriter.appendValue(localize(bundle, "jsp.controlPoint.categoryNoStage", c.getLocalizedName(game.getLocale()),
						localize(bundle, ControlPointType.getDisplayName(ri.getControlPoint().getType()))));
				break;
			case REGULAR:
			case STAGE_END:
				tagWriter.appendValue(
						localize(bundle, "jsp.controlPoint.category", c.getLocalizedName(game.getLocale()), ri.getLegNumber()));
				break;
			}

			tagWriter.appendValue("&nbsp;");
			tagWriter.startTag("span");
			tagWriter.writeAttribute("class", "badge");
			tagWriter.writeAttribute("refreshed-by", "infoRefresh");
			tagWriter.writeAttribute("refresh-key", "routeCounter_" + ri.getId());
			tagWriter.endTag();
			tagWriter.endTag();
			tagWriter.endTag();
			
			String reminder = c.getLocalizedDescription(locale);
			if (!StringUtils.isEmpty(reminder)) {
				tagWriter.startTag("p");
				tagWriter.writeAttribute("class", "volunteer-category-reminder");
				tagWriter.appendValue(reminder);
				tagWriter.endTag();
			}
		}
		tagWriter.endTag();
	}
%>
<%@ include file="../template/head.jsp"%>
<%
	LocalizationContext bundle = (LocalizationContext) pageContext.getAttribute("msg");
	Volunteer volunteer = (Volunteer) pageContext.getAttribute("volunteer", PageContext.REQUEST_SCOPE);
%>
<script>
	function onsite(input) {
		var ajaxData = {
			volunteer : ${volunteer.id},
			onsite : input.prop('checked')
		};
		
		var successHandler = function(data) {
			if (data.responseClass == 'INFO') { 
				location.reload();
				return true;
			}
			return false;
		}
		
		processToggleAjax(input, ajaxData, successHandler);
	}
	
	$(function() {
		$('#infoRefresh').click();
	});
</script>
<div class="container">
	<h1>
		<fmt:message key="jsp.controlPoint.header" bundle="${msg}">
			<fmt:param><c:out value="${volunteer.controlPoint.nameDisplayWithChildren}"/></fmt:param>
		</fmt:message>
	</h1>
	<div class="row cp-onsite-toggle-div form-group">
		<spring:url value="/api/v1/volunteer/onsite" var="onsiteAjax" />
		<input type="checkbox" data-toggle="toggle" class="cp-onsite-toggle" data-on="<fmt:message key="jsp.controlPoint.onsiteAction" bundle="${msg}" />" 
			data-off="<fmt:message key="jsp.controlPoint.offsiteAction" bundle="${msg}" />" data-onstyle="success" data-offstyle="danger" 
			onchange="onsite($(this))" ajax-target="${onsiteAjax}" <c:if test="${volunteer.active}">checked</c:if> />
	</div>
	
	<ul class="nav nav-tabs">
		<c:if test="${volunteer.active}">
			<li class="active"><a data-toggle="tab" href="#team"><fmt:message key="jsp.volunteer.teamTab" bundle="${msg}" /></a></li>
		</c:if>
		<li<c:if test="${not volunteer.active}"> class="active"</c:if>>
			<a data-toggle="tab" href="#info"><fmt:message key="jsp.volunteer.infoTab" bundle="${msg}" /></a>
		</li>
		<c:if test="${volunteer.active}">
			<li><a data-toggle="tab" href="#history"><fmt:message key="${teamEventTable.simpleTitle}" bundle="${msg}" /></a></li>
		</c:if>
	</ul>
	
	<div class="tab-content">
		<c:if test="${volunteer.active}">
			<div id="team" class="tab-pane active">
				<div class="narrow div-center">
					<c:set value="${false}" var="modal"/>
					<%@ include file="../forms/teamProcessByVolunteerForm.jsp"%>
				</div>
			</div>
		</c:if>
		<div id="info" class="tab-pane<c:if test="${not volunteer.active}"> active</c:if>">
			<div class="row">
				<spring:url value="/api/v1/controlPoint/${volunteer.controlPoint.id}/stat" var="refreshAjax" />
				<%
				String refreshAjax = (String) pageContext.getAttribute("refreshAjax");
				writeInfo(pageContext, volunteer, bundle, refreshAjax);
				%>
			</div>
			<c:if test="${volunteer.active}">
				<div class="row">
					<div class="col-sm-12">
						<h3><fmt:message key="jsp.volunteer.searchTeam" bundle="${msg}" /></h3>
						<c:set var="searchGameId" value="<%=volunteer.getVolunteerGame().getId()%>"/>
						<%@ include file="../forms/teamSearch.jsp"%>						
					</div>
				</div>
			</c:if>
		</div>
		<c:if test="${volunteer.active}">
			<div id="history" class="tab-pane">
				<runcity:table bundle="${msg}" table="${teamEventTable}" caption="false"/>
			</div>
		</c:if>
	</div>
</div>
<%@ include file="../template/foot.jsp"%>