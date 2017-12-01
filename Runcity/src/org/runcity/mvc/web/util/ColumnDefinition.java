package org.runcity.mvc.web.util;

public class ColumnDefinition {
	private String name;
	private String label;
	private Object[] substitution;

	public ColumnDefinition(String name, String label) {
		this.name = name;
		this.label = label;
	}

	public ColumnDefinition(String name, String label, Object ... substitution) {
		this(name, label);
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

	public void setSubstitution(String[] substitution) {
		this.substitution = substitution;
	}
	
	public Object[] getSubstitution() {
		return substitution;
	}
}
