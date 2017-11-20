package com.classrecorder.teacherserver.server.entities.local;

public class LocalVideo {
	
	private String videoName;
	private String urlApiLocalVideo;
	private String urlApiLocalCuts;
	private String urlApiLocalThumb;
	
	public LocalVideo(String urlApiLocalVideo, String urlApiLocalCuts, String urlApiLocalThumb) {
		this.setUrlApiLocalVideo(urlApiLocalVideo);
		this.setUrlApiLocalCuts(urlApiLocalCuts);
		this.setUrlApiLocalThumb(urlApiLocalThumb);
	}
	
	public LocalVideo() {
		setUrlApiLocalVideo(null);
		setUrlApiLocalCuts(null);
		setUrlApiLocalThumb(null);
		setVideoName(null);
	}

	public String getUrlApiLocalVideo() {
		return urlApiLocalVideo;
	}

	public void setUrlApiLocalVideo(String urlApiLocalVideo) {
		this.urlApiLocalVideo = urlApiLocalVideo;
	}

	public String getUrlApiLocalCuts() {
		return urlApiLocalCuts;
	}

	public void setUrlApiLocalCuts(String urlApiLocalCuts) {
		this.urlApiLocalCuts = urlApiLocalCuts;
	}

	public String getUrlApiLocalThumb() {
		return urlApiLocalThumb;
	}

	public void setUrlApiLocalThumb(String urlApiLocalThumb) {
		this.urlApiLocalThumb = urlApiLocalThumb;
	}

	public String getVideoName() {
		return videoName;
	}

	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}

}
