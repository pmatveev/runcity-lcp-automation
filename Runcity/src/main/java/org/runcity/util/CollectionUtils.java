package org.runcity.util;

import java.util.Collection;
import java.util.Iterator;

public class CollectionUtils {
	public static <T> Collection<T> applyChanges(Collection<T> prev, Collection<T> now) {
		Iterator<T> i = prev.iterator();
		
		while (i.hasNext()) {
			T next = i.next();
			if (!now.contains(next)) {
				i.remove();
			}
		}
		
		i = now.iterator();
		
		while (i.hasNext()) {
			T next = i.next();
			if (!prev.contains(next)) {
				prev.add(next);
			}
		}
		
		return prev;
	}
}
