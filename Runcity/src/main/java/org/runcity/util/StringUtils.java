package org.runcity.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import org.springframework.context.MessageSource;

public class StringUtils {
	public static boolean isEmpty(String s) {
		return s == null || "".equals(s);
	}

	public static boolean isEqual(String s1, String s2) {
		if (isEmpty(s1) && isEmpty(s2)) {
			return true;
		}

		if (isEmpty(s1) || isEmpty(s2)) {
			return false;
		}

		return s1.equals(s2);
	}

	public static String toNvlString(Object o) {
		return o == null ? "" : o.toString();
	}

	public static String concatNvl(String delimiter, Object... objects) {
		StringBuilder sb = new StringBuilder();

		for (Object o : objects) {
			sb.append(toNvlString(o));
			sb.append(delimiter);
		}

		return sb.toString();
	}

	public static String toString(Collection<?> c, MessageSource m, Locale l) {
		return toString(c, ", ", m.getMessage("visualization.emptyValue", null, l));
	}

	public static String toString(Collection<?> c) {
		return toString(c, ", ", "");
	}

	public static String toString(Collection<?> c, String def) {
		return toString(c, ", ", def);
	}
	
	public static String toString(Collection<?> c, String separator, String def) {
		if (c.isEmpty()) {
			return def;
		}
		StringBuilder sb = new StringBuilder();

		for (Iterator<?> i = c.iterator(); i.hasNext();) {
			sb.append(i.next());
			if (i.hasNext()) {
				sb.append(separator);
			}
		}
		
		return sb.toString();
	}
	
	public static String xss(Object o) {
		String s = o == null ? "" : o.toString();
		return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&#x27").replace("/", "&#x2F");
	}
}
