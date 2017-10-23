package org.runcity.mvc.web.formdata;

public class ConsumerForm {
	private Long id;
	private String username;
	private Boolean isActive;
	private String credentials;
	private String password;
	private String password2;
	private String email;

	public ConsumerForm() {
	}

	public ConsumerForm(Long id, String username, Boolean isActive, String credentials, String password,
			String password2, String email) {
		super();
		this.id = id;
		this.username = username;
		this.isActive = isActive;
		this.credentials = credentials;
		this.password = password;
		this.password2 = password2;
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword2() {
		return password2;
	}

	public void setPassword2(String password2) {
		this.password2 = password2;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public String getCredentials() {
		return credentials;
	}

	public void setCredentials(String credentials) {
		this.credentials = credentials;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
