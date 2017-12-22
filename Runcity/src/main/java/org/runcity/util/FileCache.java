package org.runcity.util;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.cache2k.Cache;
import org.cache2k.Cache2kBuilder;

public class FileCache {
	private final String CLEAR = "clear";
	private Cache<String, CachedFile> cache;
	
	private class CachedFile {
		private byte[] value;
		
		public CachedFile(byte[] value) {
			this.value = value;
		}
		
		public byte[] getValue() {
			return value;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + Arrays.hashCode(value);
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
			CachedFile other = (CachedFile) obj;
			if (!Arrays.equals(value, other.value))
				return false;
			return true;
		}
	}
	
	public FileCache(String name) {
		this.cache = new Cache2kBuilder<String, CachedFile>() {}
				.name(name)
				.entryCapacity(100)
				.expireAfterWrite(5, TimeUnit.MINUTES)
				.build();
	}
	
	public boolean put(String idt, byte[] value) {
		return cache.putIfAbsent(idt, new CachedFile(value));
	}
	
	public byte[] get(String idt) {
		if (idt == null || CLEAR.equals(idt)) {
			return null;
		} else {
			return cache.get(idt).getValue();
		}
	}
}
