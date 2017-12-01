package org.runcity.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DynamicLocaleList {
	private Map<String, String> locales = new HashMap<String, String>();
	
	public void addLocale(String locale, String display) {
		locales.put(locale, display);
	}
	
	public Set<String> keySet() {
		return locales.keySet();
	}
	
	public String get(Object key) {
		return locales.get(key);
	}

	public boolean containsKey(String locale) {
		return locales.containsKey(locale);
	}
	
	public Map<String, String> prepareMap() {
		Map<String, String> result = new HashMap<String, String>();
		
		for (String lc : keySet()) {
			result.put(get(lc), "");
		}
		
		return result;
	}
}
