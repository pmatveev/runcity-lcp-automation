package org.runcity.exception;

@SuppressWarnings("serial")
public class EMailException extends Exception {
	public EMailException() {
		super();
	}
	
	public EMailException(Throwable t) {
		super(t);
	}
	
	public EMailException(String s) {
		super(s);
	}
}
