package org.runcity.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DynamicLocaleList {
	private Set<String> locales = new HashSet<String>();
	
	public void add(String locale) {
		locales.add(locale);
	}
	
	public Set<String> locales() {
		return locales;
	}
	
	public boolean contains(String locale) {
		return locales.contains(locale);
	}
	
	public Map<String, String> prepareMap() {
		Map<String, String> result = new HashMap<String, String>();
		
		for (String lc : locales) {
			result.put(lc, "");
		}
		
		return result;
	}
}
