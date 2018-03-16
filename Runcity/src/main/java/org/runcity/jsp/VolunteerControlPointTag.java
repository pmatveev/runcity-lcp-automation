package org.runcity.jsp;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.jstl.fmt.LocalizationContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.runcity.db.entity.Category;
import org.runcity.db.entity.ControlPoint;
import org.runcity.db.entity.RouteItem;
import org.runcity.db.entity.Volunteer;
import org.runcity.util.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.tags.form.TagWriter;

@SuppressWarnings("serial")
public class VolunteerControlPointTag extends TagSupport {

	private LocalizationContext bundle;
	private Volunteer volunteer;
	
	public void setBundle(LocalizationContext bundle) {
		this.bundle = bundle;
	}

	public void setVolunteer(Volunteer volunteer) {
		this.volunteer = volunteer;
	}
	
	private String localize(String message, Object... args) {
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

	@Override
	public int doStartTag() throws JspException {
		ControlPoint controlPoint = volunteer.getControlPoint();
		if (controlPoint == null) {
			return SKIP_BODY;
		}

		TagWriter tagWriter = new TagWriter(pageContext);
		String locale = LocaleContextHolder.getLocale().toString();
		SimpleDateFormat dateTimeFormat = new SimpleDateFormat(localize("common.shortDateTimeFormat"));
		dateTimeFormat.setTimeZone(volunteer.getTz());
		
		// cut start
		if (volunteer.getActive()) {
			tagWriter.startTag("div");
			tagWriter.writeAttribute("class", "row cut-div");
			tagWriter.startTag("a");
			tagWriter.writeAttribute("class", "cut");
			tagWriter.writeAttribute("data-toggle", "collapse");
			tagWriter.writeAttribute("data-target", "#cpdetails");
			tagWriter.appendValue(localize("common.cut"));
			tagWriter.endTag();
			tagWriter.endTag();
			
			tagWriter.startTag("div");
			tagWriter.writeAttribute("id", "cpdetails");
			tagWriter.writeAttribute("class", "collapse");
		}
		
		// basic info
		tagWriter.startTag("form");
		tagWriter.writeAttribute("class", "form-horizontal read-form col-sm-6");
		tagWriter.startTag("h3");
		tagWriter.appendValue(localize("jsp.controlPoint.general"));
		tagWriter.endTag();
		writeField(tagWriter, "address", localize("jsp.controlPoint.address"), controlPoint.getLocalizedAddress(locale));
		writeField(tagWriter, "datefrom", localize("jsp.volunteer.from"),
				dateTimeFormat.format(volunteer.getUtcDateFrom()));
		writeField(tagWriter, "dateto", localize("jsp.volunteer.to"), dateTimeFormat.format(volunteer.getUtcDateTo()));
		tagWriter.endTag();

		// categories
		tagWriter.startTag("div");
		tagWriter.writeAttribute("class", "volunteer-categories col-sm-6");
		tagWriter.startTag("h3");
		tagWriter.appendValue(localize("jsp.controlPoint.categories"));
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

		for (RouteItem ri : routeItems) {
			tagWriter.startTag("p");
			tagWriter.writeAttribute("class", "volunteer-category");

			Category c = ri.getRoute().getCategory();

			switch (ri.getControlPoint().getType()) {
			case START:
			case BONUS:
			case FINISH:
				tagWriter.appendValue(localize("jsp.controlPoint.categoryNoStage", c.getLocalizedName(locale),
						localize(ri.getControlPoint().getType().getDisplayName())));
				break;
			case REGULAR:
			case STAGE_END:
				tagWriter.appendValue(
						localize("jsp.controlPoint.category", c.getLocalizedName(locale), ri.getLegNumber()));
				break;
			}
			
			String reminder = c.getLocalizedDescription(locale);
			if (!StringUtils.isEmpty(reminder)) {
				tagWriter.startTag("p");
				tagWriter.writeAttribute("class", "volunteer-category-reminder");
				tagWriter.appendValue(reminder);
				tagWriter.endTag();
			}

			tagWriter.endTag();
		}
		tagWriter.endTag();
		
		// cut end
		if (volunteer.getActive()) {
			tagWriter.endTag();
		}

		return SKIP_BODY;
	}
}
