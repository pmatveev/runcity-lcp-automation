package org.runcity.util;

import java.util.Arrays;

public class CachedFile {
	private byte[] value;
	
	public CachedFile() {
	}
	
	public CachedFile(byte[] value) {
		this.value = value;
	}

	public byte[] getValue() {
		return value;
	}

	public void setValue(byte[] value) {
		this.value = value;
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
