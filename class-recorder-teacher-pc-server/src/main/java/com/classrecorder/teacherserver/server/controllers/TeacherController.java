package com.classrecorder.teacherserver.server.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.classrecorder.teacherserver.server.entities.Teacher;
import com.classrecorder.teacherserver.server.entities.User;
import com.classrecorder.teacherserver.server.forms.TeacherForm;
import com.classrecorder.teacherserver.server.repository.TeacherRepository;
import com.classrecorder.teacherserver.server.security.UserComponent;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.Optional;

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
				Optional<Teacher> optionalTeacher = teacherRepository.findById(userId);
				if(!optionalTeacher.isPresent()) {
					return new ResponseEntity<>("Teacher doesn't exists", HttpStatus.NOT_FOUND);
				}
				Teacher teacher = optionalTeacher.get();
				return new ResponseEntity<>(teacher, HttpStatus.OK);
			}
		}
		if(user == null) {
			return new ResponseEntity<>("Teacher is not logged", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>("User logged is not a teacher", HttpStatus.BAD_REQUEST);
    }
    
    @JsonView(TeacherViewAll.class)
    @RequestMapping(value="/api/registerTeacher", method=RequestMethod.POST)
    public ResponseEntity<?> registerTeacher(@RequestBody TeacherForm teacherBody) throws Exception {
        String userName = teacherBody.getUserName();
        String password = teacherBody.getPassword();
        String email = teacherBody.getEmail();
        String fullName = teacherBody.getFullName();
        Teacher teacher = new Teacher(userName, password, fullName, email, "ROLE_TEACHER");
        if(teacherRepository.findByUserName(userName).size() > 0 || teacherRepository.findByEmail(email).size() > 0) {
            return new ResponseEntity<>("Teacher actually exist", HttpStatus.CONFLICT);
        } 
        teacherRepository.save(teacher);
        return new ResponseEntity<>(teacher, HttpStatus.OK);
    }
}
