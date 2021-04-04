package org.marco.calamai.todolist.model;

public class User {
	
	private String username;
	private String psw;
	
	public User(String username, String psw) {
		this.username = username;
		this.psw = psw;
	}

	public String getUsername() {
		return username;
	}

	public String getPsw() {
		return psw;
	}
	

}
