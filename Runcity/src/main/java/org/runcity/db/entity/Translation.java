package org.runcity.db.entity;

import javax.persistence.*;

import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "translation")
@FilterDefs(value = { @FilterDef(name = "category", defaultCondition = "ref_table='category'") })
public class Translation {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "id", columnDefinition = "int", length = 18, nullable = false)
	private Long id;

	@Column(name = "ref_table", length = 32, nullable = false)
	private String refTable;

	@Column(name = "ref_record", columnDefinition = "int", length = 18, nullable = false)
	private Long refRecord;

	@Column(name = "locale", length = 32, nullable = false)
	private String locale;

	@Column(name = "content", length = 255, nullable = false)
	private String content;

	public Translation() {
	}

	public Translation(Long id, String refTable, Long refRecord, String locale, String content) {
		this.id = id;
		this.refTable = refTable;
		this.refRecord = refRecord;
		this.locale = locale;
		this.content = content;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRefTable() {
		return refTable;
	}

	public void setRefTable(String refTable) {
		this.refTable = refTable;
	}

	public Long getRefRecord() {
		return refRecord;
	}

	public void setRefRecord(Long refRecord) {
		this.refRecord = refRecord;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
