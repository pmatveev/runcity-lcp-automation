package org.runcity.util;

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
	
	public static String concatNvl(String delimiter, Object ... objects) {
		StringBuilder sb = new StringBuilder();
		
		for (Object o : objects) {
			sb.append(toNvlString(o));
			sb.append(delimiter);
		}
		
		return sb.toString();
	}
}
