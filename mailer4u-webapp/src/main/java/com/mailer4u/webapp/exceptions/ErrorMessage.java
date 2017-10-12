package com.mailer4u.webapp.exceptions;

public class ErrorMessage {
	public int status;
	public String name;
	public String message;
	public String developerMessage;
	public String moreInfoURL;
	public int code;
	
	public ErrorMessage() {
		
	}
	
	public ErrorMessage(String url , Exception ex){
		this.message = ex.getMessage();
		this.moreInfoURL = "This URL Has Error : " + url;
	}
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getDeveloperMessage() {
		return developerMessage;
	}
	public void setDeveloperMessage(String developerMessage) {
		this.developerMessage = developerMessage;
	}
	public String getMoreInfoURL() {
		return moreInfoURL;
	}
	public void setMoreInfoURL(String moreInfoURL) {
		this.moreInfoURL = moreInfoURL;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	
	
}
