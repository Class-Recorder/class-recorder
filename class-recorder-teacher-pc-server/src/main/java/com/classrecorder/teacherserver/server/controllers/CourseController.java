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
		Teacher teacher = teacherRepository.findOne(teacherId);
		if(teacher == null) {
			return new ResponseEntity<>("Teacher Not Found", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(teacher.getCoursesCreated(), HttpStatus.OK);
    }

    @JsonView(courseBasicInfo.class)
    @RequestMapping(value="/api/getCourseById/{courseId}", method=RequestMethod.GET)
    public ResponseEntity<?> getCourseById(@PathVariable long courseId) throws Exception {
        Course course = courseRepository.findOne(courseId);
        if(course == null) {
            return new ResponseEntity<>("Course not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(course, HttpStatus.OK);
    }
    
    @JsonView(courseBasicInfo.class)
    @RequestMapping(value="/api/postCourse/{teacherId}", method=RequestMethod.POST)
    public ResponseEntity<?> postCourseByTeacherId(@RequestBody CourseForm courseBody, @PathVariable long teacherId) throws Exception {
        Teacher teacher = this.teacherRepository.findOne(teacherId);
        if(teacher == null) {
            return new ResponseEntity<>("Teacher not found to add course", HttpStatus.NOT_FOUND);
        }
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
        Course course = courseRepository.findOne(courseId);
        if(course == null) {
            return new ResponseEntity<>("Course not found", HttpStatus.NOT_FOUND);
        }
        course.setName(courseBody.getName());
        course.setDescription(courseBody.getDescription());
        courseRepository.save(course);
        return new ResponseEntity<>(course, HttpStatus.OK);
    }
	
    @JsonView(courseBasicInfo.class)
    @RequestMapping(value="api/deleteCourse/{courseId}", method=RequestMethod.DELETE)
    public ResponseEntity<?> methodName(@PathVariable long courseId) throws Exception {
        if(!courseRepository.exists(courseId)) {
            return new ResponseEntity<>("Course not found", HttpStatus.NOT_FOUND);
        }
        courseRepository.delete(courseId);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

}
