package com.classrecorder.teacherserver.youtube;

import java.util.List;

public class YoutubeVideoInfo {
	
	private String videoPath;
	private String privacyStatus;
	private String videoTitle;
	private String description;
	private List<String> tags;
	
	public YoutubeVideoInfo() {
		
	}
	
	public YoutubeVideoInfo(String videoPath, String privacyStatus, String videoTitle, String description,
			List<String> tags) {
		super();
		this.videoPath = videoPath;
		this.privacyStatus = privacyStatus;
		this.videoTitle = videoTitle;
		this.description = description;
		this.tags = tags;
	}

	public String getVideoPath() {
		return videoPath;
	}

	public void setVideoPath(String videoPath) {
		this.videoPath = videoPath;
	}

	public String getPrivacyStatus() {
		return privacyStatus;
	}

	public void setPrivacyStatus(String privacyStatus) {
		this.privacyStatus = privacyStatus;
	}

	public String getVideoTitle() {
		return videoTitle;
	}

	public void setVideoTitle(String videoTitle) {
		this.videoTitle = videoTitle;
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

}
