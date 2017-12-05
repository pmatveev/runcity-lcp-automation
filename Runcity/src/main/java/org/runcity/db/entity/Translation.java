package org.runcity.db.entity;

import javax.persistence.*;

import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.GenericGenerator;
import org.runcity.db.entity.util.DBEntity;
import org.runcity.util.StringUtils;
import org.springframework.util.ObjectUtils;

@Entity
@Table(name = "translation")
@FilterDefs(value = { @FilterDef(name = "category", defaultCondition = "ref_table='category' and ref_column='name'") })
public class Translation implements DBEntity {
	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	@Column(name = "id", columnDefinition = "int", length = 18, nullable = false)
	private Long id;

	@Column(name = "ref_table", length = 32, nullable = false)
	private String refTable;

	@Column(name = "ref_column", length = 32, nullable = false)
	private String refCol;

	@Column(name = "ref_record", columnDefinition = "int", length = 18)
	private Long refRecord;

	@Column(name = "locale", length = 32, nullable = false)
	private String locale;

	@Column(name = "content", length = 255, nullable = false)
	private String content;

	public Translation() {
	}

	public Translation(Long id, String refTable, String refCol, Long refRecord, String locale, String content) {
		setId(id);
		setRefTable(refTable);
		setRefCol(refCol);
		setRefRecord(refRecord);
		setLocale(locale);
		setContent(content);
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

	public String getRefCol() {
		return refCol;
	}

	public void setRefCol(String refCol) {
		this.refCol = refCol;
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

	@Override
	public int hashCode() {
		return StringUtils.concatNvl(".", refTable, refCol, refRecord, locale, content).hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Translation)) {
			return false;
		}

		Translation t = (Translation) o;
		return StringUtils.isEqual(refTable, t.refTable) && StringUtils.isEqual(refCol, t.refCol)
				&& ObjectUtils.nullSafeEquals(refRecord, t.refRecord) && StringUtils.isEqual(locale, t.locale)
				&& StringUtils.isEqual(content, t.content);
	}
}
