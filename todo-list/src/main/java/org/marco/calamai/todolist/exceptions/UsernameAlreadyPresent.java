package org.marco.calamai.todolist.exceptions;

public class UsernameAlreadyPresent extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public UsernameAlreadyPresent() {
		super();
	}
	
	public UsernameAlreadyPresent(String message) {
		super(message);
	}
	
	public UsernameAlreadyPresent(String message, Throwable cause) {
		super(message, cause);
	}
	
	public 	UsernameAlreadyPresent(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super (message, cause, enableSuppression, writableStackTrace);
	}
	
	public UsernameAlreadyPresent(Throwable cause) {
		super(cause);
	}

}
