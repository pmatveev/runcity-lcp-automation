package org.runcity.exception;

@SuppressWarnings("serial")
public class DBException extends Exception {
	public DBException() {
		super();
	}
	
	public DBException(Throwable t) {
		super(t);
	}
}
