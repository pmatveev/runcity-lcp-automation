package org.runcity.util;

public class Version {
	private String name;
	private String version;
	
	public Version(String version) {
		this.name = "version";
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public String getVersion() {
		return version;
	}
}
