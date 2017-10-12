package com.mailer4u.webapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value=HttpStatus.NOT_ACCEPTABLE, reason="Email Or Password Not Correct, Or probably your account is locked by security")
public class AuthenticationException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	public AuthenticationException() {
		
	}
	
	public AuthenticationException(String message){
		super(message);
	}
}
