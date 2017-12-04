package org.runcity.exception;

@SuppressWarnings("serial")
public class UnexpectedArgumentException extends RuntimeException {
	public UnexpectedArgumentException(String message) {
		super(message);
	}
}
