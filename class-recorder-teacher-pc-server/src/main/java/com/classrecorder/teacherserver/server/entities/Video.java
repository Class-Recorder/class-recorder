package com.classrecorder.teacherserver.server.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
public class Video {
	
	public interface Basic {}
	
	public interface CourseInfo extends Course.Basic {};
	
	@JsonView(Basic.class)
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	long id;
	
	@JsonView(Basic.class)
	private String youtubeId;
	
	private String title;
	
	@Column(name="description", length=2048)
	private String description;
	
	@JsonView(Basic.class)
	@ElementCollection
	@CollectionTable(name ="tags")
	private List<String> tags = new ArrayList<>();

	@JsonView(CourseInfo.class)
	@ManyToOne
	private Course course;

	public Video() {}

	public Video(String youtubeId, String title, String description, Course course) {
		super();
		this.youtubeId = youtubeId;
		this.title = title;
		this.description = description;
		this.course = course;
	}



	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getYoutubeId() {
		return youtubeId;
	}

	public void setYoutubeId(String youtubeId) {
		this.youtubeId = youtubeId;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Video other = (Video) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
}
