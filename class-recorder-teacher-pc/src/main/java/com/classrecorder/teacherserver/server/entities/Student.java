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
@DiscriminatorValue(value = "student")
public class Student extends User{
	
	public interface Basic {};
	
	public interface CourseInfo extends Course.Basic {}
	
	@JsonView(CourseInfo.class)
	@ManyToMany(cascade= {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy="subscribers")
	private List<Course> subscribed = new ArrayList<>();
	
	public Student() {}

	public Student(String userName, String password, String fullName, String email, String... roles) {
		super(userName, password, fullName, email, Arrays.asList(roles));
	}

	public List<Course> getSubscribed() {
		return subscribed;
	}

	public void setSubscribed(List<Course> subscribed) {
		this.subscribed = subscribed;
	}

	
	
	
	
}
