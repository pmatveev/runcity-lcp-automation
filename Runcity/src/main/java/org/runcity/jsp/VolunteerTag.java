package org.runcity.jsp;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.jstl.fmt.LocalizationContext;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.taglibs.standard.tag.el.core.UrlTag;
import org.runcity.db.entity.Game;
import org.runcity.db.entity.Volunteer;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.tags.form.TagWriter;

@SuppressWarnings("serial")
public class VolunteerTag extends TagSupport {

	private LocalizationContext bundle;

	private Collection<Volunteer> data;

	public void setBundle(LocalizationContext bundle) {
		this.bundle = bundle;
	}

	public void setData(Collection<Volunteer> data) {
		this.data = data;
	}

	private String localize(String message) {
		return bundle.getResourceBundle().getString(message);
	}

	private void processUrl(String url, String var) throws JspException {
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

	@Override
	public int doStartTag() throws JspException {
		SimpleDateFormat dateFormat = new SimpleDateFormat(localize("common.shortDateFormat"));
		SimpleDateFormat timeFormat = new SimpleDateFormat(localize("common.shortTimeFormat"));
		TagWriter tagWriter = new TagWriter(pageContext);

		if (data == null || data.size() == 0) {
			tagWriter.startTag("div");
			tagWriter.writeAttribute("class", "volunteer-entry");
			tagWriter.startTag("p");
			tagWriter.writeAttribute("class", "game-nogames");
			tagWriter.appendValue(localize("jsp.volunteer.nogames"));
			tagWriter.endTag();
			tagWriter.endTag();
			return SKIP_BODY;
		}

		for (Volunteer v : data) {
			switch (v.getVolunteerType()) {
			case COORDINATOR:
				processUrl("/secure/coordinatorDetails/" + v.getVolunteerGame().getId(), "vurl");
				break;
			case VOLUNTEER:
				processUrl("/secure/volunteerDetails/" + v.getControlPoint().getId(), "vurl");
				break;
			}
			String url = pageContext.getAttribute("vurl").toString();

			Game g = v.getVolunteerGame();
			dateFormat.setTimeZone(g.getTz());
			timeFormat.setTimeZone(g.getTz());

			tagWriter.startTag("div");
			tagWriter.writeAttribute("class", "volunteer-entry");

			String label;
			String labelContent;
			Date now = new Date();
			if (now.compareTo(g.getUtcDateFrom()) < 0) {
				label = "label-warning";
				labelContent = localize("jsp.game.waiting");
			} else if (now.compareTo(g.getUtcDateTo()) < 0) {
				label = "label-success";
				labelContent = localize("jsp.game.running");
			} else {
				label = "label-danger";
				labelContent = localize("jsp.game.closed");
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
				tagWriter.appendValue(localize("jsp.game.current"));
				tagWriter.endTag();				
			}
			
			tagWriter.endTag();

			Date from = v.getUtcDateFrom();
			Date to = v.getUtcDateTo();
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
		return SKIP_BODY;
	}
}
