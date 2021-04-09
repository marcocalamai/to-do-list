package org.marco.calamai.todolist.exceptions;

public class ToDoNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public ToDoNotFoundException() {
		super();
	}
	
	public ToDoNotFoundException(String message) {
		super(message);
	}
	
	public ToDoNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public 	ToDoNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super (message, cause, enableSuppression, writableStackTrace);
	}
	
	public ToDoNotFoundException(Throwable cause) {
		super(cause);
	}
	
	

}
