<%@page import="java.util.TimeZone"%>
<%@page import="org.springframework.context.i18n.LocaleContextHolder"%>
<%@page import="org.springframework.util.ObjectUtils"%>
<%@page import="java.util.Date"%>
<%@page import="org.runcity.db.entity.Game"%>
<%@page import="org.springframework.web.servlet.tags.form.TagWriter"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="org.runcity.db.entity.Volunteer"%>
<%@page import="org.springframework.web.servlet.tags.UrlTag"%>
<%@page import="javax.servlet.jsp.jstl.fmt.LocalizationContext"%>
<%@page import="java.util.Collection"%>
<%!
	private String localize(LocalizationContext bundle, String message) {
		return bundle.getResourceBundle().getString(message);
	}
	
	private void processUrl(PageContext pageContext, String url, String var) throws JspException {
		if (url != null) {
			UrlTag urlTag = new UrlTag();
			urlTag.setValue(url);
			urlTag.setVar(var);
	
			urlTag.setPageContext(pageContext);
			urlTag.doStartTag();
			urlTag.doEndTag();
		} else {
			pageContext.setAttribute(var, null);
		}
	}
	
	private void writeEvents(PageContext pageContext, LocalizationContext bundle, Collection<Volunteer> data) throws JspException  {
		SimpleDateFormat dateFormat = new SimpleDateFormat(localize(bundle, "common.shortDateFormat"));
		SimpleDateFormat timeFormat = new SimpleDateFormat(localize(bundle, "common.shortTimeFormat"));
		
		TagWriter tagWriter = new TagWriter(pageContext);

		if (data == null || data.size() == 0) {
			tagWriter.startTag("div");
			tagWriter.writeAttribute("class", "volunteer-entry");
			tagWriter.startTag("p");
			tagWriter.writeAttribute("class", "game-nogames");
			tagWriter.appendValue(localize(bundle, "jsp.volunteer.nogames"));
			tagWriter.endTag();
			tagWriter.endTag();
			return;
		}

		for (Volunteer v : data) {
			switch (v.getVolunteerType()) {
			case COORDINATOR:
				processUrl(pageContext, "/secure/coordinatorDetails/" + v.getVolunteerGame().getId(), "vurl");
				break;
			case VOLUNTEER:
				processUrl(pageContext, "/secure/volunteerDetails/" + v.getControlPoint().getId(), "vurl");
				break;
			}
			String url = pageContext.getAttribute("vurl").toString();

			Game g = v.getVolunteerGame();
			dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

			tagWriter.startTag("div");
			tagWriter.writeAttribute("class", "volunteer-entry");

			String label;
			String labelContent;
			Date now = new Date();
			if (now.compareTo(g.getUtcDateFrom()) < 0) {
				label = "label-warning";
				labelContent = localize(bundle, "jsp.game.waiting");
			} else if (now.compareTo(g.getUtcDateTo()) < 0) {
				label = "label-success";
				labelContent = localize(bundle, "jsp.game.running");
			} else {
				label = "label-danger";
				labelContent = localize(bundle, "jsp.game.closed");
			}

			tagWriter.startTag("p");
			tagWriter.writeAttribute("class", "game-caption");
			tagWriter.startTag("a");
			tagWriter.writeAttribute("href", url);
			if (v.getControlPoint() == null) {
				tagWriter.appendValue(g.getName());
			} else {
				tagWriter.appendValue(g.getName() + " (" + v.getControlPoint().getName() + ")");
			}
			tagWriter.endTag();
			tagWriter.appendValue(" ");
			tagWriter.startTag("span");
			tagWriter.writeAttribute("class", "label " + label);
			tagWriter.appendValue(labelContent);
			tagWriter.endTag();
			
			// isActive may be null for coordinators!
			if (Boolean.TRUE.equals(v.getActive())) {
				tagWriter.appendValue(" ");
				tagWriter.startTag("span");
				tagWriter.writeAttribute("class", "label label-info");
				tagWriter.appendValue(localize(bundle, "jsp.game.current"));
				tagWriter.endTag();				
			}
			
			tagWriter.endTag();

			Date from = v.getDateFrom();
			Date to = v.getDateTo();
			String dateFrom = dateFormat.format(from);
			String timeFrom = timeFormat.format(from);
			String dateTo = dateFormat.format(to);
			String timeTo = timeFormat.format(to);
			String date;

			if (ObjectUtils.nullSafeEquals(dateFrom, dateTo)) {
				date = dateFrom + " " + timeFrom + " - " + timeTo;
			} else {
				date = dateFrom + " " + timeFrom + " - " + dateTo + " " + timeTo;
			}

			tagWriter.startTag("p");
			tagWriter.writeAttribute("class", "game-sub");
			tagWriter.appendValue(date);
			tagWriter.endTag();

			if (v.getControlPoint() != null) {
				tagWriter.startTag("p");
				tagWriter.writeAttribute("class", "game-sub");
				tagWriter.appendValue(v.getControlPoint().getLocalizedAddress(LocaleContextHolder.getLocale().toString()));
				tagWriter.endTag();
			}

			tagWriter.startTag("p");
			tagWriter.writeAttribute("class", "game-sub");
			tagWriter.appendValue(g.getCity() + ", " + g.getCountry());
			tagWriter.endTag();
			tagWriter.endTag();
		}
	}
%>
<%@ include file="../template/head.jsp"%>
<%
	LocalizationContext bundle = (LocalizationContext) pageContext.getAttribute("msg");
	Collection<Volunteer> volunteerData = (Collection<Volunteer>) pageContext.getAttribute("volunteerData", PageContext.REQUEST_SCOPE);
	Collection<Volunteer> coordinatorData = (Collection<Volunteer>) pageContext.getAttribute("coordinatorData", PageContext.REQUEST_SCOPE);
%>
<div class="container">
	<h1><fmt:message key="jsp.volunteer.header" bundle="${msg}" /></h1>
	<div class="tabbable">
		<ul class="nav nav-tabs">
			<li class="active"><a data-toggle="tab" href="#volTab"><fmt:message key="jsp.volunteer.lcpTab" bundle="${msg}" /></a></li>
			<li><a data-toggle="tab" href="#coordTab"><fmt:message key="jsp.volunteer.coordinatorTab" bundle="${msg}" /></a></li>
		</ul>
		
		<div class="tab-content">
			<div id="volTab" class="tab-pane active">
				<% writeEvents(pageContext, bundle, volunteerData); %>
			</div>
			<div id="coordTab" class="tab-pane">
				<% writeEvents(pageContext, bundle, coordinatorData); %>
			</div>
		</div>
	</div>
</div>
<%@ include file="../template/foot.jsp"%>