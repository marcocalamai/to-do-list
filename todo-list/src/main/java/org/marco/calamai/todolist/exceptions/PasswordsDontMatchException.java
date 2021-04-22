package org.marco.calamai.todolist.exceptions;

public class PasswordsDontMatchException extends IllegalArgumentException {
	
	private static final long serialVersionUID = 1L;
	
	
	public PasswordsDontMatchException() {
		super();
	}

	public PasswordsDontMatchException(String message) {
		super(message);
	}
	
	public PasswordsDontMatchException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public PasswordsDontMatchException(Throwable cause) {
		super(cause);
	}
}
