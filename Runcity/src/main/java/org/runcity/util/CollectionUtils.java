package org.runcity.util;

import java.util.List;

public class CollectionUtils {
	public static <T> List<T> applyChanges(List<T> prev, List<T> now) {
		// remove absent
		for (int i = 0; i < prev.size(); i++) {
			if (!now.contains(prev.get(i))) {
				prev.remove(i);
				i--;
			}
		}

		// add new
		for (int i = 0; i < now.size(); i++) {
			if (!prev.contains(now.get(i))) {
				prev.add(now.get(i));
			}
		}
		
		return prev;
	}
}
