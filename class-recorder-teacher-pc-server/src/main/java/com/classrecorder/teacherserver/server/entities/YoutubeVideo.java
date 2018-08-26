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
public class YoutubeVideo {
	
	public interface Basic {}
	
	public interface CourseInfo extends Course.Basic {};
	
	@JsonView(Basic.class)
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    long id;
    
    @JsonView(Basic.class)
    private String youtubeId;
	
	@JsonView(Basic.class)
    private String link;
    
    @JsonView(Basic.class)
    private String imageLink;
    
    @JsonView(Basic.class)
	private String title;
    
    @JsonView(Basic.class)
	@Column(name="description", length=2048)
	private String description;
	
	@JsonView(Basic.class)
	@ElementCollection
	@CollectionTable(name ="tags")
	private List<String> tags = new ArrayList<>();

	@JsonView(CourseInfo.class)
	@ManyToOne
	private Course course;

	public YoutubeVideo() {}

	public YoutubeVideo(String link, String title, String description, Course course) {
		super();
		this.link = link;
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

    public String getYoutubeId() {
        return this.youtubeId;
    }

    public void setYoutubeId(String youtubeId) {
        this.youtubeId = youtubeId;
    }
    
    public String getLink() {
        return this.link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImageLink() {
        return this.imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
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
		YoutubeVideo other = (YoutubeVideo) obj;
		if (id != other.id)
			return false;
		return true;
    }
    
    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", youtubeId='" + getYoutubeId() + "'" +
            ", link='" + getLink() + "'" +
            ", imageLink='" + getImageLink() + "'" +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", tags='" + getTags() + "'" +
            ", course='" + getCourse() + "'" +
            "}";
    }
	
}
