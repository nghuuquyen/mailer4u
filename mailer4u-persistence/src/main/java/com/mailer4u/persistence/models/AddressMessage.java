package com.mailer4u.persistence.models;

public class AddressMessage {
	private String name;
	private String email;
	
	public AddressMessage(String name, String email) {
		this.name = name;
		this.email = email;
	}
	public AddressMessage() {
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}