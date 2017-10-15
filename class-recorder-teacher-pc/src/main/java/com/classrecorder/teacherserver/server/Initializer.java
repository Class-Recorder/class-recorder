package com.classrecorder.teacherserver.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;

import com.classrecorder.teacherserver.modules.ffmpegwrapper.formats.FfmpegAudioFormat;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.formats.FfmpegContainerFormat;
import com.classrecorder.teacherserver.modules.ffmpegwrapper.formats.FfmpegVideoFormat;
import com.classrecorder.teacherserver.modules.youtube.YoutubeVideoInfo;
import com.classrecorder.teacherserver.server.entities.Course;
import com.classrecorder.teacherserver.server.entities.Student;
import com.classrecorder.teacherserver.server.entities.Teacher;
import com.classrecorder.teacherserver.server.entities.User;
import com.classrecorder.teacherserver.server.entities.Video;
import com.classrecorder.teacherserver.server.repository.CourseRepository;
import com.classrecorder.teacherserver.server.repository.StudentRepository;
import com.classrecorder.teacherserver.server.repository.TeacherRepository;
import com.classrecorder.teacherserver.server.repository.UserRepository;
import com.classrecorder.teacherserver.server.repository.VideoRepository;
import com.classrecorder.teacherserver.server.services.FfmpegService;
import com.classrecorder.teacherserver.server.services.YoutubeService;

@Controller
public class Initializer implements CommandLineRunner {
	
	@Autowired
	private FfmpegService ffmpeg;
	
	@Autowired
	private YoutubeService youtubeService;
	
	@Autowired
	private TeacherRepository teacherRepository;
	
	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired
	private CourseRepository courseRepository;
	
	@Autowired
	private VideoRepository videoRepository;
	
	boolean recording;
	
	
	@Override
	public void run(String... args) throws Exception {
		
		Teacher teacher1 = new Teacher("Teacher1", "1234", "Juan Rodriguez", "juan@juan.com", "ROLE_TEACHER");
		Teacher teacher2 = new Teacher("Teacher2", "1234", "Alberto Ruiz", "alberto@alberto.com","ROLE_TEACHER");
		Teacher teacher3 = new Teacher("Teacher1", "1234", "Juan Pérez", "juan@juan.com", "ROLE_TEACHER");
		Student student1 = new Student("Student1", "1234", "Rick Sanchez", "rick@rick.com", "ROLE_STUDENT");
		Student student2 = new Student("Student2", "1234", "Gonzalo Alvarez", "gonzalo@gonzalo.com", "ROLE_STUDENT");
		
		Course course = new Course("Curso de Programación", "Esto es un curso de programación");
		Course course2 = new Course("Curso del teacher 3", "Otro curso");
		
		//Teachers and courses
		teacher1.getCoursesCreated().add(course);
		teacher2.getCoursesCreated().add(course);
		course.getTeacherCreators().add(teacher1);
		course.getTeacherCreators().add(teacher2);
		
		teacher3.getCoursesCreated().add(course2);
		course2.getTeacherCreators().add(teacher3);
		
		//Subscribers
		student1.getSubscribed().add(course);
		student1.getSubscribed().add(course2);
		course.getSubscribers().add(student1);
		course2.getSubscribers().add(student2);
		
		teacherRepository.save(teacher1);
		teacherRepository.save(teacher2);
		teacherRepository.save(teacher3);
		studentRepository.save(student1);
		studentRepository.save(student2);
		
	}
}
