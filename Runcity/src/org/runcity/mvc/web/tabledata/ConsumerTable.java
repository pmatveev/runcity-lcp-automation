package org.runcity.mvc.web.tabledata;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.runcity.db.entity.Consumer;
import org.runcity.db.entity.ConsumerRole;
import org.runcity.db.service.ConsumerService;
import org.runcity.mvc.rest.util.Views;
import org.runcity.mvc.web.util.ColumnDefinition;
import org.runcity.mvc.web.util.FormListboxActiveColumn;
import org.runcity.mvc.web.util.FormListboxUserRoleColumn;
import org.runcity.util.StringUtils;
import org.springframework.context.MessageSource;

import com.fasterxml.jackson.annotation.JsonView;

public class ConsumerTable extends AbstractTable {
	@JsonView(Views.Public.class)
	private List<TableRow> data = new LinkedList<TableRow>();

	protected class TableRow {
		@JsonView(Views.Public.class)
		private String id;

		@JsonView(Views.Public.class)
		private String username;

		@JsonView(Views.Public.class)
		private String credentials;

		@JsonView(Views.Public.class)
		private String email;

		@JsonView(Views.Public.class)
		private String active;

		@JsonView(Views.Public.class)
		private String roles;

		public TableRow(Consumer c, MessageSource messageSource, Locale l) {
			this.id = StringUtils.xss(c.getId().toString());
			this.username = StringUtils.xss(c.getUsername());
			this.credentials = StringUtils.xss(c.getCredentials());
			this.email = StringUtils.xss(c.getEmail());
			this.active = FormListboxActiveColumn.getOptionDisplay(c.isActive().toString(), messageSource, l);

			List<String> rolesTmp = new LinkedList<String>();
			for (ConsumerRole r : c.getRoles()) {
				rolesTmp.add(FormListboxUserRoleColumn.getOptionDisplay(r.getCode(), messageSource, l));
			}
			Collections.sort(rolesTmp);
			this.roles = StringUtils.xss(StringUtils.toString(rolesTmp, messageSource, l));
		}

		public String getId() {
			return id;
		}

		public String getUsername() {
			return username;
		}

		public String getCredentials() {
			return credentials;
		}

		public String getEmail() {
			return email;
		}

		public String getActive() {
			return active;
		}

		public String getRoles() {
			return roles;
		}
	}

	public ConsumerTable() {
		this.columns.add(new ColumnDefinition("id", null));
		this.columns.add(new ColumnDefinition("username", "user.username"));
		this.columns.add(new ColumnDefinition("credentials", "user.credentials"));
		this.columns.add(new ColumnDefinition("email", "user.email"));
		this.columns.add(new ColumnDefinition("active", "user.active"));
		this.columns.add(new ColumnDefinition("roles", "user.roles"));
		this.ajaxData = "/api/v1/consumerTable";
		this.ajaxButtons = "/api/v1/consumerTableButtons";
		this.id = "consumerTable";
	}
	
	public ConsumerTable(MessageSource messageSource) {
		this();
		super.setMessageSource(messageSource);
	}

	public void fetchAll(ConsumerService service) {
		List<Consumer> consumers = service.selectAll();
		for (Consumer c : consumers) {
			data.add(new TableRow(c, messageSource, locale));
		}
	}

	public List<TableRow> getData() {
		return data;
	}
}
