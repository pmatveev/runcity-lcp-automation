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
}
