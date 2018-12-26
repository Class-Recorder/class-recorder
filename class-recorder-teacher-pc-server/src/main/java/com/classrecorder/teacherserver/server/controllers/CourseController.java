package com.classrecorder.teacherserver.server.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.classrecorder.teacherserver.server.entities.Course;
import com.classrecorder.teacherserver.server.entities.Teacher;
import com.classrecorder.teacherserver.server.forms.CourseForm;
import com.classrecorder.teacherserver.server.repository.CourseRepository;
import com.classrecorder.teacherserver.server.repository.TeacherRepository;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.Optional;

@RestController
public class CourseController {

    private interface courseAllInfo extends Course.Basic, Course.VideoInfo {}
    
    private interface courseBasicInfo extends Course.Basic {}
	
	@Autowired
    private TeacherRepository teacherRepository;
    
    @Autowired
    private CourseRepository courseRepository;
	
	@JsonView(courseAllInfo.class)
	@RequestMapping("/api/getCoursesByTeacherId/{teacherId}")
	public ResponseEntity<?> getCoursesByTeacherId(@PathVariable long teacherId){
		Optional<Teacher> optionalTeacher = teacherRepository.findById(teacherId);
		if(!optionalTeacher.isPresent()) {
			return new ResponseEntity<>("Teacher Not Found", HttpStatus.NOT_FOUND);
		}
		Teacher teacher = optionalTeacher.get();
		return new ResponseEntity<>(teacher.getCoursesCreated(), HttpStatus.OK);
    }

    @JsonView(courseBasicInfo.class)
    @RequestMapping(value="/api/getCourseById/{courseId}", method=RequestMethod.GET)
    public ResponseEntity<?> getCourseById(@PathVariable long courseId) throws Exception {
	    Optional<Course> optionalCourse = courseRepository.findById(courseId);
        if(!optionalCourse.isPresent()) {
            return new ResponseEntity<>("Course not found", HttpStatus.NOT_FOUND);
        }
        Course course = optionalCourse.get();
        return new ResponseEntity<>(course, HttpStatus.OK);
    }
    
    @JsonView(courseBasicInfo.class)
    @RequestMapping(value="/api/postCourse/{teacherId}", method=RequestMethod.POST)
    public ResponseEntity<?> postCourseByTeacherId(@RequestBody CourseForm courseBody, @PathVariable long teacherId) throws Exception {
        Optional<Teacher> optionalTeacher = this.teacherRepository.findById(teacherId);
        if(!optionalTeacher.isPresent()) {
            return new ResponseEntity<>("Teacher not found to add course", HttpStatus.NOT_FOUND);
        }
        Teacher teacher = optionalTeacher.get();
        Course course = new Course();
        course.setName(courseBody.getName());
        course.setDescription(courseBody.getDescription());
        course.getTeacherCreators().add(teacher);
        courseRepository.save(course);
        return new ResponseEntity<>(course, HttpStatus.OK);
    }

    @JsonView(courseBasicInfo.class)
    @RequestMapping(value="/api/updateCourse/{courseId}", method=RequestMethod.PUT)
    public ResponseEntity<?> updateCourseByTeacherId(@RequestBody CourseForm courseBody, @PathVariable long courseId) throws Exception {
        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        if(!optionalCourse.isPresent()) {
            return new ResponseEntity<>("Course not found", HttpStatus.NOT_FOUND);
        }
        Course course = optionalCourse.get();
        course.setName(courseBody.getName());
        course.setDescription(courseBody.getDescription());
        courseRepository.save(course);
        return new ResponseEntity<>(course, HttpStatus.OK);
    }
	
    @JsonView(courseBasicInfo.class)
    @RequestMapping(value="api/deleteCourse/{courseId}", method=RequestMethod.DELETE)
    public ResponseEntity<?> methodName(@PathVariable long courseId) throws Exception {
        if(!courseRepository.existsById(courseId)) {
            return new ResponseEntity<>("Course not found", HttpStatus.NOT_FOUND);
        }
        courseRepository.deleteById(courseId);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

}
