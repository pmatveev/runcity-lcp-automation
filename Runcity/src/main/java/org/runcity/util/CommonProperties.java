package org.runcity.util;

public class CommonProperties {
	private int cacheTime;
	private int passwordTokenLifetime;
	private String url;
	
	public int getCacheTime() {
		return cacheTime;
	}

	public void setCacheTime(int cacheTime) {
		this.cacheTime = cacheTime;
	}

	public int getPasswordTokenLifetime() {
		return passwordTokenLifetime;
	}

	public void setPasswordTokenLifetime(int passwordTokenLifetime) {
		this.passwordTokenLifetime = passwordTokenLifetime;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
