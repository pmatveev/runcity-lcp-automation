package org.runcity.mvc.web.util;

public class ColumnDefinition {
	private String name;
	private String label;
	private String groupLabel;
	private Object[] substitution;

	public ColumnDefinition(String name, String label) {
		this.name = name;
		this.label = label;
	}

	public ColumnDefinition(String name, String groupLabel, String label, Object ... substitution) {
		this(name, label);
		this.groupLabel = groupLabel;
		this.substitution = substitution;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getGroupLabel() {
		return groupLabel;
	}

	public void setGroupLabel(String groupLabel) {
		this.groupLabel = groupLabel;
	}

	public void setSubstitution(String[] substitution) {
		this.substitution = substitution;
	}
	
	public Object[] getSubstitution() {
		return substitution;
	}
}
