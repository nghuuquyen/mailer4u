package com.mailer4u.webapp.controllers;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mailer4u.persistence.models.User;
import com.mailer4u.persistence.models.UserAuthentication;
import com.mailer4u.services.MailService;
import com.mailer4u.webapp.exceptions.AuthenticationException;

@Controller
public class HomeController {

	@Autowired
	User user;

	@Autowired
	MailService mailService;

	@RequestMapping("/login")
	public ModelAndView openLoginPage(ModelAndView mv) {
		mv.setViewName("views/login");
		return mv;
	}

	@RequestMapping("/")
	public ModelAndView home(ModelAndView mv) {
		mv.setViewName("views/mail-box");
		return mv;
	}

	@ResponseBody
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<?> login(@RequestBody User userData) {
		user.setEmail(userData.getEmail());
		user.setPassword(userData.getPassword());

		if (mailService.login(user)) {

			// Just remove this password.
			userData.setPassword("");
			return new ResponseEntity<>(new UserAuthentication("Login Success", new Date(), userData), HttpStatus.OK);
		}
		throw new AuthenticationException();
	}

}
