package org.marco.calamai.todolist.exceptions;

public class EmptyRegistrationFieldsException extends IllegalArgumentException {

	private static final long serialVersionUID = 1L;
	
	public EmptyRegistrationFieldsException() {
		super();
	}

	public EmptyRegistrationFieldsException(String message) {
		super(message);
	}
	
	public EmptyRegistrationFieldsException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public EmptyRegistrationFieldsException(Throwable cause) {
		super(cause);
	}

}
