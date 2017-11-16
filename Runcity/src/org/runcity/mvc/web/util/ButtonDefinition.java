package org.runcity.mvc.web.util;

import java.util.LinkedList;
import java.util.List;

import org.runcity.mvc.rest.util.RestGetResponseBody;
import org.runcity.mvc.rest.util.Views;
import org.springframework.context.MessageSource;

import com.fasterxml.jackson.annotation.JsonView;

public class ButtonDefinition extends RestGetResponseBody {
	@JsonView(Views.Public.class)
	private List<Button> buttons = new LinkedList<Button>();
		
	public class Button {
		@JsonView(Views.Public.class)
		private String text;
		
		@JsonView(Views.Public.class)
		private String className;
		
		@JsonView(Views.Public.class)
		private String action;

		@JsonView(Views.Public.class)
		private String extend;

		public Button(String text, String className, String action, String extend) {
			this.text = messageSource.getMessage(text, null, locale);
			this.className = className;
			this.action = action;
			this.extend = extend;
		}

		public String getText() {
			return text;
		}

		public String getClassName() {
			return className;
		}

		public String getAction() {
			return action;
		}

		public String getExtend() {
			return extend;
		}
	}

	public ButtonDefinition(MessageSource messageSource) {
		super(messageSource);
	}
	
	public List<Button> getButtons() {
		return buttons;
	}

	public void addButton(Button button) {
		this.buttons.add(button);
	}
}
