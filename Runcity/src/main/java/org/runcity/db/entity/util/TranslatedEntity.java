package org.runcity.db.entity.util;

import java.util.List;

import org.runcity.db.entity.Translation;

public abstract class TranslatedEntity<T> {
	public abstract T cloneForAdd();
	
	protected void updateRef(List<Translation> l, Long id) {
		for (Translation t : l) {
			t.setRefRecord(id);
		}
	}
}
