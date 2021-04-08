package org.marco.calamai.todolist.model;

import java.math.BigInteger;

public class User {
	
	private BigInteger id;
	private String username;
	private String psw;
	
	public User(String username, String psw) {
		this.username = username;
		this.psw = psw;
	}
	
	public BigInteger getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getPsw() {
		return psw;
	}
	
}
