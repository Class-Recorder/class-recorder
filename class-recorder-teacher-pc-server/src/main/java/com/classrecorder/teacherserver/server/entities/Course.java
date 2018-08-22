package com.classrecorder.teacherserver.server.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
public class Course {
	
	public interface Basic {}
	
	public interface TeacherInfo extends Teacher.Basic {}
	
	public interface VideoInfo extends YoutubeVideo.Basic {}
	
	public interface StudentInfo extends Student.Basic {}
	
	@JsonView(Basic.class)
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@JsonView(Basic.class)
	private String name;
	
	@JsonView(Basic.class)
	@Lob 
	@Column(name="description", length=1024)
	private String description;
	
	@JsonView(TeacherInfo.class)
	@ManyToMany(cascade= {CascadeType.PERSIST, CascadeType.MERGE})
	private List<Teacher> teacherCreators = new ArrayList<>();
	
	@JsonView(VideoInfo.class)
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "course", orphanRemoval = true, fetch = FetchType.EAGER)
	private List<YoutubeVideo> videos = new ArrayList<>();
	
	@JsonView(StudentInfo.class)
	@ManyToMany(cascade= {CascadeType.PERSIST, CascadeType.MERGE})
	private List<Student> subscribers = new ArrayList<>();

	public Course() {}
	
	public Course(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Teacher> getTeacherCreators() {
		return teacherCreators;
	}

	public void setTeacherCreators(List<Teacher> teacherCreators) {
		this.teacherCreators = teacherCreators;
	}

	public List<YoutubeVideo> getVideos() {
		return videos;
	}

	public void setVideos(List<YoutubeVideo> videos) {
		this.videos = videos;
	}

	public List<Student> getSubscribers() {
		return subscribers;
	}

	public void setSubscribers(List<Student> subscribers) {
		this.subscribers = subscribers;
	}
	
	
	/*
	 * Returns true if both entities has the same id
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Course other = (Course) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
