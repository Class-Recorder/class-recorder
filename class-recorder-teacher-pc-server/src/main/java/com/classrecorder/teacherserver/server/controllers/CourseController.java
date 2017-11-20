package com.classrecorder.teacherserver.server.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.classrecorder.teacherserver.server.entities.Course;
import com.classrecorder.teacherserver.server.entities.Teacher;
import com.classrecorder.teacherserver.server.repository.TeacherRepository;
import com.fasterxml.jackson.annotation.JsonView;

@RestController
public class CourseController {

	private interface courseAllInfo extends Course.Basic, Course.VideoInfo {}
	
	@Autowired
	private TeacherRepository teacherRepository;
	
	@JsonView(courseAllInfo.class)
	@RequestMapping("/api/getCoursesByTeacherId/{teacherId}")
	public ResponseEntity<?> getCoursesByTeacherId(@PathVariable long teacherId){
		Teacher teacher = teacherRepository.findOne(teacherId);
		if(teacher == null) {
			return new ResponseEntity<>("Teacher Not Found", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(teacher.getCoursesCreated(), HttpStatus.OK);
	}
	


}
