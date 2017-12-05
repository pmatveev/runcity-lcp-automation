package org.runcity.util;

import org.runcity.db.entity.util.DBEntity;

public class ObjectUtils extends org.springframework.util.ObjectUtils {
	public static boolean equals(DBEntity o1, DBEntity o2) {
		if (o1 == null && o2 == null) {
			return true;
		}
		
		if (o1 == null || o2 == null) {
			return false;
		}
		
		return nullSafeEquals(o1.getId(), o2.getId());
	}
}
