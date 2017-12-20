package org.runcity.db.entity;

import java.util.Collection;
import java.util.Iterator;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Index;
import org.runcity.db.entity.util.DBEntity;
import org.runcity.util.StringUtils;

@Entity
@Table(name = "translation")
@org.hibernate.annotations.Table(appliesTo = "translation", indexes = {
		@Index(name = "translation_ref", columnNames = { "ref_record", "ref_column", "ref_table" }) })
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

	@Column(name = "content", length = 4000, nullable = false)
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
		final int prime = 31;
		int result = 1;
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + ((locale == null) ? 0 : locale.hashCode());
		result = prime * result + ((refCol == null) ? 0 : refCol.hashCode());
		result = prime * result + ((refRecord == null) ? 0 : refRecord.hashCode());
		result = prime * result + ((refTable == null) ? 0 : refTable.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Translation other = (Translation) obj;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (locale == null) {
			if (other.locale != null)
				return false;
		} else if (!locale.equals(other.locale))
			return false;
		if (refCol == null) {
			if (other.refCol != null)
				return false;
		} else if (!refCol.equals(other.refCol))
			return false;
		if (refRecord == null) {
			if (other.refRecord != null)
				return false;
		} else if (!refRecord.equals(other.refRecord))
			return false;
		if (refTable == null) {
			if (other.refTable != null)
				return false;
		} else if (!refTable.equals(other.refTable))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return locale + ": " + content;
	}

	public static String getDisplay(Collection<Translation> c, String locale) {
		String second = null;
		String any = null;

		Iterator<Translation> i = c.iterator();
		while (i.hasNext()) {
			Translation t = i.next();
			if (StringUtils.isEmpty(t.content)) {
				continue;
			}
			if (StringUtils.isEqual(locale, t.locale)) {
				return t.content;
			}
			if (second == null && StringUtils.isEqualPrefix(locale, t.locale, 2)) {
				second = t.content + " [" + t.locale + "]";
			}
			if (any == null) {
				any = t.content + " [" + t.locale + "]";
			}
		}

		return second != null ? second : any;
	}
}
