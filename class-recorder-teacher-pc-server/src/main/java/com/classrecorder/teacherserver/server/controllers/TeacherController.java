package com.classrecorder.teacherserver.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.classrecorder.teacherserver.server.entities.Teacher;
import com.classrecorder.teacherserver.server.entities.User;
import com.classrecorder.teacherserver.server.repository.TeacherRepository;
import com.classrecorder.teacherserver.server.security.UserComponent;
import com.fasterxml.jackson.annotation.JsonView;

@RestController
public class TeacherController {
	
	private interface TeacherViewAll extends User.Basic, Teacher.Basic, Teacher.CourseInfo{}
	
	@Autowired
	private TeacherRepository teacherRepository;
	
	@Autowired
	private UserComponent userComponent;
	
	@JsonView(TeacherViewAll.class)
	@RequestMapping(value = "/api/teacherInfo/{userId}", method = RequestMethod.GET)
	public ResponseEntity<?> getTeacherInfo(@PathVariable long userId){
		User user = null;
		if(userComponent.isLoggedUser() ) {
			user = userComponent.getLoggedUser();
			if(user.getId() == userId && user.getRoles().contains("ROLE_TEACHER")) {
				Teacher teacher = teacherRepository.findOne(userId);
				return new ResponseEntity<Teacher>(teacher, HttpStatus.OK);
			}
		}
		if(user == null) {
			return new ResponseEntity<>("Teacher is not logged", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>("User logged is not a teacher", HttpStatus.BAD_REQUEST);
	}
}
