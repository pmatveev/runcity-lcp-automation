package org.runcity.mvc.web.util;

public class ColumnDefinition {
	public enum ColumnFormat {DATE, DATETIME, DATETIMESTAMP, TIMESTAMP, IMAGE}
	
	private String name;
	private String label;
	private String groupLabel;
	private Object[] substitution;

	// options
	private boolean hidden = false;
	private ColumnFormat format;
	private String sort;
	private Integer sortIndex;
	private String imageUrl;
	
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

	public Boolean isVisible() {
		return !hidden;
	}

	public ColumnDefinition setHidden(boolean hidden) {
		this.hidden = hidden;
		return this;
	}

	public ColumnFormat getFormat() {
		return format;
	}

	public ColumnDefinition setDateFormat() {
		this.format = ColumnFormat.DATE;
		return this;
	}

	public ColumnDefinition setDateTimeFormat() {
		this.format = ColumnFormat.DATETIME;
		return this;
	}

	public ColumnDefinition setDateTimeStampFormat() {
		this.format = ColumnFormat.DATETIMESTAMP;
		return this;
	}

	public ColumnDefinition setTimeStampFormat() {
		this.format = ColumnFormat.TIMESTAMP;
		return this;
	}
	
	public ColumnDefinition setImageFormat(String url) {
		this.format = ColumnFormat.IMAGE;
		this.imageUrl = url;
		return this;
	}

	public String getSort() {
		return sort;
	}

	public Integer getSortIndex() {
		return sortIndex;
	}

	public ColumnDefinition setSort(String sort, int index) {
		this.sort = sort;
		this.sortIndex = index;
		return this;
	}
	
	public String getImageUrl() {
		return imageUrl;
	}
}
