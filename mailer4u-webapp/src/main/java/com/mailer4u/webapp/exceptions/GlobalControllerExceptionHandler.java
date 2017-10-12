package com.mailer4u.webapp.exceptions;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@ControllerAdvice
@EnableWebMvc
public class GlobalControllerExceptionHandler {

	@ExceptionHandler(value = MailNotFoundException.class)
	public ResponseEntity<ErrorMessage> handleEmailNotFoundException(HttpServletRequest request, Exception ex) {
		return new ResponseEntity<ErrorMessage>(new ErrorMessage(request.getRequestURI(), ex),
				HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(value = AuthenticationException.class)
	public ResponseEntity<ErrorMessage> handleAuthenticationException(HttpServletRequest request, Exception ex) {
		return new ResponseEntity<ErrorMessage>(new ErrorMessage(request.getRequestURI(), ex),
				HttpStatus.NOT_ACCEPTABLE);
	}
}
