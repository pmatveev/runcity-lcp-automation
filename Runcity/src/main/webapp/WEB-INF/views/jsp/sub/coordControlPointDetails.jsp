<%@page import="org.runcity.db.entity.enumeration.ControlPointType"%>
<%@page import="org.runcity.db.entity.Category"%>
<%@page import="org.runcity.db.entity.Game"%>
<%@page import="java.util.Comparator"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.TimeZone"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="org.springframework.context.i18n.LocaleContextHolder"%>
<%@page import="org.runcity.db.entity.ControlPoint"%>
<%@page import="org.springframework.web.servlet.tags.form.TagWriter"%>
<%@page import="org.springframework.web.servlet.tags.UrlTag"%>
<%@page import="org.runcity.db.entity.RouteItem"%>
<%@page import="java.text.MessageFormat"%>
<%@page import="javax.servlet.jsp.jstl.fmt.LocalizationContext"%>
<%@ include file="../template/init.jsp"%>
<%!
	private String localize(LocalizationContext bundle, String message, Object... args) {
		String result = bundle.getResourceBundle().getString(message);

		if (args == null) {
			return result;
		}

		return MessageFormat.format(result, args);
	}
	
	private String composeTeamsUrl(PageContext pageContext, ControlPoint controlPoint, RouteItem routeItem) throws JspException {
		String url = "/secure/controlPoint/" + controlPoint.getId() + "/teams" + (routeItem == null ? "" : "?routeItem=" + routeItem.getId());
		UrlTag tag = new UrlTag();
		tag.setValue(url);
		tag.setVar("teamUrl");
		tag.setPageContext(pageContext);
		tag.doStartTag();
		tag.doEndTag();
		return (String) pageContext.getAttribute("teamUrl");
	}
	
	private void writeInfo(PageContext pageContext, ControlPoint controlPoint, LocalizationContext bundle, String refreshAjax) throws JspException {
		TagWriter tagWriter = new TagWriter(pageContext);
		String locale = LocaleContextHolder.getLocale().toString();
		SimpleDateFormat dateTimeFormat = new SimpleDateFormat(localize(bundle, "common.shortDateTimeFormat"));
		dateTimeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			
		// categories
		tagWriter.startTag("div");
		tagWriter.writeAttribute("class", "volunteer-categories");
		tagWriter.startTag("div");
		tagWriter.writeAttribute("class", "pull-right");
		tagWriter.startTag("button");
		tagWriter.writeAttribute("id", "infoRefresh");
		tagWriter.writeAttribute("class", "btn btn-default ajax-refresh");
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
				return o1.getRoute().getCategory().getLocalizedName(locale)
						.compareTo(o2.getRoute().getCategory().getLocalizedName(locale));
			}
		});

		Game game = controlPoint.getGame();
		tagWriter.startTag("a");
		tagWriter.writeAttribute("href", composeTeamsUrl(pageContext, controlPoint, null));
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
			tagWriter.writeAttribute("href", composeTeamsUrl(pageContext, controlPoint, ri));
			tagWriter.startTag("p");
			tagWriter.writeAttribute("class", "volunteer-category");

			Category c = ri.getRoute().getCategory();

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
		}
		tagWriter.endTag();
	}
%>
<%
	LocalizationContext bundle = (LocalizationContext) pageContext.getAttribute("msg");
	ControlPoint controlPoint = (ControlPoint) pageContext.getAttribute("controlPoint", PageContext.REQUEST_SCOPE);
%>
<ul class="nav nav-tabs">
	<li class="active"><a data-toggle="tab" href="#${prefix}volTab"><fmt:message key="${coordVolunteerTableByCP.simpleTitle}" bundle="${msg}" /></a></li>
	<li><a data-toggle="tab" href="#${prefix}statisticsTab"><fmt:message key="coordinator.cpStats" bundle="${msg}" /></a></li>
</ul>

<div id="${prefix}modalForms" class="div-modal">
	<c:set value="${true}" var="modal"/>
	<c:set value="${coordVolunteerTableByCP}" var="currTable"/>
	<%@ include file="../forms/volunteerCreateEditByCPForm.jsp"%>
</div>

<div class="tab-content">
	<div id="${prefix}volTab" class="tab-pane active">
		<runcity:table bundle="${msg}" table="${coordVolunteerTableByCP}" caption="false"/>
	</div>
	<div id="${prefix}statisticsTab" class="tab-pane">
		<spring:url value="/api/v1/controlPoint/${controlPoint.id}/stat" var="refreshAjax" />
		<%
			String refreshAjax = (String) pageContext.getAttribute("refreshAjax");
			writeInfo(pageContext, controlPoint, bundle, refreshAjax); 
		%>
	</div>
</div>