package org.marco.calamai.todolist.exceptions;

public class WhitespaceInRegistrationFieldsException extends IllegalArgumentException {

	private static final long serialVersionUID = 1L;
	
	
	public WhitespaceInRegistrationFieldsException() {
		super();
	}

	public WhitespaceInRegistrationFieldsException(String message) {
		super(message);
	}
	
	public WhitespaceInRegistrationFieldsException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public WhitespaceInRegistrationFieldsException(Throwable cause) {
		super(cause);
	}

}
