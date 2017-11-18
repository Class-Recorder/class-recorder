package com.classrecorder.teacherserver.server.security;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.classrecorder.teacherserver.server.entities.User;
import com.fasterxml.jackson.annotation.JsonView;
/**
 * This class is used to provide REST endpoints to logIn and logOut to the
 * service. These endpoints are used by Angular 2 SPA client application.
 * 
 * NOTE: This class is not intended to be modified by app developer.
 */
@RestController
public class LoginController {

	private static final Logger log = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private UserComponent userComponent;

	@JsonView(User.Basic.class)
	@RequestMapping("/api/logIn")
	public ResponseEntity<User> logIn() throws Exception{
		
		if (!userComponent.isLoggedUser()) {
			log.info("Not user logged");
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		} else {
			User loggedUser = userComponent.getLoggedUser();
			log.info("Logged as " + loggedUser.getUserName());
			return new ResponseEntity<>(loggedUser, HttpStatus.OK);
		}
	}

	@RequestMapping("/api/logOut")
	public ResponseEntity<Boolean> logOut(HttpSession session) throws Exception{

		if (!userComponent.isLoggedUser()) {
			log.info("No user logged");
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		} else {
			session.invalidate();
			log.info("Logged out");
			return new ResponseEntity<>(true, HttpStatus.OK);
		}
	}

}