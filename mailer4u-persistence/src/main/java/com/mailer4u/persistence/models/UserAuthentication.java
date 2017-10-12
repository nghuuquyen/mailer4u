package com.mailer4u.persistence.models;

import java.util.Date;

public class UserAuthentication {
	
	private String message;
	private Date lastLogin;
	private User user;
	
	public UserAuthentication(){
		
	}
	
	public UserAuthentication(String message, Date lastLogin, User user) {
		super();
		this.message = message;
		this.lastLogin = lastLogin;
		this.user = user;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Date getLastLogin() {
		return lastLogin;
	}
	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	
}
