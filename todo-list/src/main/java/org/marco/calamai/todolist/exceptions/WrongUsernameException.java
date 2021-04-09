package org.marco.calamai.todolist.exceptions;

public class WrongUsernameException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public WrongUsernameException() {
		super();
	}
	
	public WrongUsernameException(String message) {
		super(message);
	}
	
	public WrongUsernameException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public 	WrongUsernameException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super (message, cause, enableSuppression, writableStackTrace);
	}
	
	public WrongUsernameException(Throwable cause) {
		super(cause);
	}

}
