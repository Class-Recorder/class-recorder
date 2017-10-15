package com.classrecorder.teacherserver.server.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@DiscriminatorValue(value = "teacher")
public class Teacher extends User{
	
	public interface Basic {};
	
	public interface CourseInfo extends Course.Basic {}
	
	@JsonView(CourseInfo.class)
	@ManyToMany(cascade= {CascadeType.PERSIST,CascadeType.MERGE}, 
			mappedBy="teacherCreators")
	private List<Course> coursesCreated = new ArrayList<>();
	
	public Teacher() {}
	
	public Teacher(String userName, String password, String fullName, String email, String... roles) {
		super(userName, password, fullName, email, new ArrayList<>(Arrays.asList(roles)));
	}

	public List<Course> getCoursesCreated() {
		return coursesCreated;
	}

	public void setCoursesCreated(List<Course> coursesCreated) {
		this.coursesCreated = coursesCreated;
	}
	
}
