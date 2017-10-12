package com.mailer4u.webapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE, reason = "No Such Email")
public class MailNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public MailNotFoundException() {

	}

	public MailNotFoundException(String message) {
		super(message);
	}
}
